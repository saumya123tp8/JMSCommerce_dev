# JMSCommerce

Full-stack e-commerce platform built with **Java, Spring Boot, Spring Security, JPA/Hibernate, MySQL, Redis, React, Flyway and Razorpay**.

JMSCommerce is a production-oriented e-commerce application covering product discovery, category hierarchy, variants, customizations, Redis cart management, checkout, COD/online payments, orders, reviews and customer-support reports.

## Table of Contents

- Overview
- Features
- Technology Stack
- Architecture
- Domain Model
- Authentication & Authorization
- Product Catalog
- Category & Specification Inheritance
- Variants
- Customization
- Cart & Redis
- Orders
- Payments & Razorpay
- Inventory Reservation
- Reviews
- Order Reports
- Database & Flyway
- API Design
- Validation & Exceptions
- Concurrency
- Observability
- Project Structure
- Local Setup
- Configuration
- API Modules
- Frontend
- Testing
- Engineering Decisions
- Future Improvements

---

## Overview

The backend exposes REST APIs under:

```text
/api/v1
```

Main domains:

```text
Users
Authentication
Authorization
Categories
Brands
Products
Specifications
Variants
Customizations
Cart
Orders
Payments
Reviews
Order Reports
Admin Operations
```

The backend follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
JPA/Hibernate
    ↓
MySQL

          ┌──────────────┐
          │    Redis     │
          │ Cart / TTL   │
          └──────────────┘

          ┌──────────────┐
          │   Razorpay   │
          │   Payments   │
          └──────────────┘
```

---

# Features

### Customer

- JWT authentication
- Access/refresh token flow
- Refresh-token rotation
- OAuth2 support for Google and GitHub
- User profile and verification support
- Address management
- Product/category/brand browsing
- Product specifications
- Product variants
- Product customization
- Redis-backed cart
- Checkout
- COD and online payment
- Razorpay integration
- Payment retry
- Order tracking
- Product reviews
- Order issue reporting
- User/admin report conversation
- Report resolutions

### Admin / Developer

- Product management
- Category management
- Brand management
- Variant management
- Customization management
- Order operations
- Payment operations
- User management
- Order-report management
- Report status transitions
- Admin responses
- Report resolution
- Application monitoring

---

# Technology Stack

| Layer | Technology |
|---|---|
| Language | Java |
| Backend | Spring Boot |
| Security | Spring Security |
| Authentication | JWT + OAuth2 |
| ORM | JPA / Hibernate |
| Database | MySQL |
| Cache / Cart | Redis |
| Migration | Flyway |
| Payments | Razorpay |
| Validation | Jakarta Bean Validation |
| API | REST |
| Frontend | React |
| Routing | React Router |
| HTTP Client | Axios |
| Monitoring | Spring Boot Actuator / Micrometer |
| Build | Gradle |

---

# Architecture

```text
                    React Frontend
                          │
                       REST API
                          │
                          ▼
                ┌─────────────────────┐
                │     Controllers     │
                └──────────┬──────────┘
                           │
                           ▼
                ┌─────────────────────┐
                │       Services      │
                │                     │
                │ Business Logic      │
                │ Transactions        │
                │ Validation          │
                │ Authorization       │
                └───────┬─────┬───────┘
                        │     │
                        ▼     ▼
                 Repositories  Razorpay
                        │
                        ▼
                     MySQL

                        ▲
                        │
                     Redis
```

The project uses DTOs and adapters around the persistence model so controllers do not need to expose JPA entities directly.

---

# Domain Model

```text
User
 ├── Roles
 ├── Addresses
 ├── Orders
 └── Reports

Category
 └── Product
      ├── Brand
      ├── Specifications
      ├── Customizations
      └── ProductVariant
            │
            ▼
         OrderItem
            │
            ▼
          Order
          ├── Payment
          │     └── PaymentAttempt
          └── DeliveryAddress
```

---

# Authentication & Authorization

JWT authentication is implemented with access and refresh tokens.

The access token contains information such as:

```text
email / subject
roles
token type
issuer
issued-at
expiration
JTI
```

Refresh tokens are persisted server-side and rotated.

### Refresh flow

```text
Refresh Token
      ↓
Verify JWT
      ↓
Validate token type
      ↓
Find stored token by JTI
      ↓
Check revoked / expired
      ↓
Validate associated user
      ↓
Revoke old token
      ↓
Generate new JTI
      ↓
Create new refresh token
      ↓
Create new access token
```

Administration uses role-based authorization, including:

```text
ROLE_ADMIN
ROLE_DEVELOPER
```

Protected operations use rules such as:

```java
@PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
```

---

# Product Catalog

Products contain:

```text
name
currency
primaryImage
shortDescription
description
category
brand
specifications
inventoryType
```

Product creation uses Jakarta validation such as:

```java
@NotBlank
@NotNull
@Size
```

Product names are normalized and slugs are generated.

Brand association is optional.

---

# Category & Specification Inheritance

Categories are hierarchical.

Example:

```text
Beverages
   │
   └── Hot Coffee
          │
          └── Cappuccino
```

Categories contain information such as:

```text
id
name
slug
description
status
parentId
level
```

Specification definitions can be inherited through the category hierarchy.

For example, a specification defined for `Coffee` can be available to a product under:

```text
Beverages
  ↓
Coffee
  ↓
Hot Coffee
  ↓
Cappuccino
```

The product service resolves allowed specifications through the parent hierarchy and validates submitted specification IDs.

This avoids requiring every descendant category to duplicate the same specification definition.

---

# Product Variants

A product can contain multiple variants.

Example:

```text
Cappuccino
 ├── Small / Hot
 ├── Medium / Hot
 ├── Large / Hot
 └── Large / Cold
```

A variant supports:

```text
MRP
sellingPrice
stock
SKU
barcode
attributes
displayName
```

The variant service validates:

- SKU uniqueness
- Barcode uniqueness
- Duplicate variants
- Required attributes
- Product/variant relationship

Variant updates also synchronize product pricing.

---

# Customization

Customization groups are attached to a product.

Example:

```text
Cappuccino
 ├── Milk
 │    ├── Regular Milk
 │    └── Almond Milk +₹20
 │
 └── Toppings
      ├── Chocolate Syrup +₹20
      └── Caramel Drizzle +₹25
```

A group supports:

```text
name
selectionType
required
minSelection
maxSelection
displayOrder
options
```

An option supports:

```text
name
adjustmentType
adjustmentValue
displayOrder
```

Example:

```json
{
  "groups": [
    {
      "name": "Milk",
      "selectionType": "SINGLE",
      "required": true,
      "minSelection": 1,
      "maxSelection": 1,
      "displayOrder": 1,
      "options": [
        {
          "name": "Regular Milk",
          "adjustmentType": "FIXED",
          "adjustmentValue": 0,
          "displayOrder": 1
        },
        {
          "name": "Almond Milk",
          "adjustmentType": "FIXED",
          "adjustmentValue": 20,
          "displayOrder": 2
        }
      ]
    }
  ]
}
```

The cart request can send selected customization option IDs.

---

# Cart & Redis

Redis is used for cart management.

```text
Frontend
   ↓
Cart API
   ↓
Cart Service
   ↓
Redis
```

The cart is temporary state while the order is being prepared.

Current configuration includes:

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      timeout: 60000
      cart:
        ttl-days: 30
```

The project also considers Redis persistence through:

```text
RDB
AOF
```

RDB provides snapshots, while AOF records write operations.

Permanent order information remains in MySQL.

---

# Orders

An order contains information including:

```text
orderNumber
status
paymentStatus
subtotal
discount
tax
deliveryCharge
grandTotal
currency
deliveryAddress
user
payment
```

Order items store historical commerce information:

```text
variant
quantity
mrp
sellingPrice
customizationPrice
totalPrice
sku
variantName
productName
inventoryReserved
```

This preserves important order snapshots even if product information changes later.

## Order lifecycle

```text
PENDING
   ↓
CONFIRMED
   ↓
PROCESSING
   ↓
SHIPPED
   ↓
DELIVERED
```

Cancellation is supported from applicable earlier states.

Terminal states include:

```text
DELIVERED
CANCELLED
```

Invalid transitions are rejected by the status-transition validator.

---

# Payments & Razorpay

The payment model separates the overall payment from individual attempts:

```text
Order
  │
  ▼
Payment
  │
  ├── Attempt #1 → FAILED
  ├── Attempt #2 → FAILED
  └── Attempt #3 → SUCCESS
```

A `PaymentAttempt` stores:

```text
amount
currency
status
razorpayOrderId
razorpayPaymentId
razorpaySignature
initiatedAt
paidAt
failedAt
failureCode
failureMessage
version
```

## COD flow

```text
Create Order
    ↓
Payment = PENDING
Order = CONFIRMED
    ↓
Cart cleared
```

## Online payment flow

```text
Create Order
    ↓
Order = PENDING
Payment = PENDING
    ↓
Initiate Payment
    ↓
Create PaymentAttempt
    ↓
Create Razorpay Order
    ↓
Razorpay Checkout
    ↓
Verify Signature
    ↓
Payment SUCCESS
    ↓
Order PaymentStatus = SUCCESS
    ↓
Order = CONFIRMED
```

## Payment APIs

```http
POST  /api/v1/payments/orders/{orderId}/initiate
POST  /api/v1/payments/verify
POST  /api/v1/payments/webhook
POST  /api/v1/payments/orders/{orderId}/retry
PATCH /api/v1/payments/orders/{orderId}/attempts/{attemptId}/cancel
```

Razorpay webhooks are signature-verified and handle events such as:

```text
payment.captured
payment.failed
```

Webhook processing is idempotent for already-successful attempts.

Payment retries create a new `PaymentAttempt` while preserving the same order/payment record.

---

# Inventory Reservation

`OrderItem` contains:

```text
inventoryReserved
```

This allows the order flow to track whether inventory for the ordered variant has been reserved.

Inventory state is intentionally separated from payment state so online-payment orders can exist before successful payment.

---

# Reviews

The application supports product ratings and reviews.

The product detail frontend loads reviews independently and can refresh the review list after a review is submitted.

---

# Order Reports

Customers can report issues against their own orders.

Supported reasons include:

```text
WRONG_ITEM_DELIVERED
ORDER_NOT_RECEIVED
POOR_PACKAGING
LATE_DELIVERY
OTHER
```

A report contains:

```text
order
reason
description
status
messages
resolution
createdAt
updatedAt
```

## Report lifecycle

```text
OPEN
  ↓
IN_REVIEW
  ↓
RESOLVED
  ↓
CLOSED
```

Rejected reports can also proceed toward closure.

## Conversation

Messages identify the sender:

```text
USER
ADMIN
```

Example:

```text
USER:
"I received the wrong item."

ADMIN:
"We are checking this issue with our delivery team."

RESOLUTION:
"Refund has been initiated."
```

Resolution types include:

```text
REFUND
REPLACEMENT
CREDIT
COMPENSATION
NO_ACTION
OTHER
```

The complete workflow has been tested:

```text
Create report
    ↓
View report
    ↓
User message
    ↓
Admin views report
    ↓
Status → IN_REVIEW
    ↓
Admin message
    ↓
Resolve report
    ↓
User sees resolution
```

---

# Database & Flyway

MySQL is the primary relational database.

JPA/Hibernate manages persistence.

Major entities include:

```text
User
Role
Address
Category
Brand
Product
ProductVariant
SpecificationDefinition
ProductSpecificationValue
VariantAttribute
CustomizationGroup
CustomizationOption
Order
OrderItem
OrderDeliveryAddress
Payment
PaymentAttempt
Review
OrderReport
OrderReportMessage
OrderReportResolution
```

A common `BaseEntity` provides fields such as:

```text
id
createdAt
updatedAt
deletedAt
createdBy
updatedBy
version
```

where applicable.

Flyway is configured for database migrations:

```yaml
spring:
  flyway:
    enable: true
    location: classpath:db/migration
```

Migration files live under:

```text
src/main/resources/db/migration
```

---

# API Design

The API base path is:

```text
/api/v1
```

Responses use a common wrapper:

```json
{
  "success": true,
  "message": "Operation message",
  "data": {},
  "error": null,
  "timestamp": "2026-08-13T21:34:59.9819756",
  "path": null
}
```

This gives the frontend a consistent response contract.

---

# Validation & Exception Handling

DTO validation uses Jakarta Bean Validation:

```java
@NotBlank
@NotNull
@NotEmpty
@Size
@Positive
@PositiveOrZero
@Min
@Max
```

Business validation covers:

```text
Duplicate products
SKU uniqueness
Barcode uniqueness
Duplicate variants
Specification validity
Order ownership
Payment ownership
Payment signature
Order status transitions
Report status transitions
```

The application uses custom exceptions including:

```text
BadRequestException
ResourceNotFoundException
PaymentGatewayException
BadCredentialsCustomException
```

A global exception handler converts exceptions into consistent API responses.

---

# Concurrency & Data Integrity

The project uses optimistic locking/versioning through the common persistence model.

This helps protect against lost updates in concurrent operations.

Payment processing also verifies that payment identifiers belong to the expected payment attempt before changing payment state.

Transactional service methods are used for important multi-step operations such as:

```text
Product creation
Variant updates
Payment verification
Payment webhook processing
Payment retry
Order/report workflows
```

---

# Observability

Spring Boot Actuator/Micrometer is used for application monitoring.

Useful endpoints include:

```text
/actuator/health
/actuator/info
/actuator/metrics
/actuator/prometheus
/actuator/flyway
```

Metrics can expose JVM, HTTP and application runtime information.

Prometheus can be used for metrics scraping and Grafana for visualization.

Sensitive endpoints should not be publicly exposed:

```text
/actuator/env
/actuator/configprops
/actuator/beans
/actuator/heapdump
/actuator/logfile
/actuator/shutdown
```

---

# Project Structure

```text
JMSCommerce/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/JMSCommerce/
│   │   │   │   ├── Adapters/
│   │   │   │   ├── Auth/
│   │   │   │   ├── Controller/
│   │   │   │   ├── DTOs/
│   │   │   │   ├── Exception/
│   │   │   │   ├── Model/
│   │   │   │   ├── Repositories/
│   │   │   │   ├── Services/
│   │   │   │   ├── Utility/
│   │   │   │   └── config/
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── db/migration/
│   │   └── test/
│   └── build.gradle
│
└── frontend/
    ├── src/
    ├── public/
    ├── package.json
    └── vite.config.*
```

---

# Local Development

## Prerequisites

- Java
- Gradle
- MySQL
- Redis
- Node.js / npm
- Razorpay test credentials for online-payment testing

## Database

```sql
CREATE DATABASE practice_springjpa;
```

Configure MySQL through environment variables/local configuration.

## Redis

Run Redis locally on:

```text
localhost:6379
```

## Backend

```bash
cd backend
./gradlew build
./gradlew bootRun
```

Windows:

```powershell
gradlew.bat build
```

Default backend URL:

```text
http://localhost:8080
```

## Frontend

```bash
cd frontend
npm install
npm run dev
```

Default frontend URL:

```text
http://localhost:5173
```

---

# Configuration

Use environment variables for secrets.

Recommended variables:

```text
JWT_SECRET
JWT_ISSUER
JWT_TTL
JWT_REFRESH_TTL

GOOGLE_CLIENT_ID
GOOGLE_CLIENT_SECRET

GITHUB_CLIENT_ID
GITHUB_CLIENT_SECRET

RAZORPAY_KEY_ID
RAZORPAY_KEY_SECRET
RAZORPAY_WEBHOOK_SECRET
```

Example:

```yaml
razorpay:
  key-id: ${RAZORPAY_KEY_ID}
  key-secret: ${RAZORPAY_KEY_SECRET}
  webhook-secret: ${RAZORPAY_WEBHOOK_SECRET}
```

Never commit:

```text
database passwords
JWT secrets
OAuth secrets
Razorpay secrets
webhook secrets
production credentials
```

---

# API Modules

## Categories

```http
GET    /api/v1/categories
GET    /api/v1/categories/{id}
POST   /api/v1/categories
PUT    /api/v1/categories/{id}
DELETE /api/v1/categories/{id}
```

Browsing endpoints are public; administration requires appropriate authorization.

## Brands

```http
GET    /api/v1/brand
GET    /api/v1/brand/{id}
POST   /api/v1/brand
PUT    /api/v1/brand/{id}
DELETE /api/v1/brand/{id}
```

## Products

```http
GET    /api/v1/products
POST   /api/v1/products
GET    /api/v1/products/{id}
GET    /api/v1/products/{id}/details
GET    /api/v1/products/{id}/specifications
GET    /api/v1/products/search
PUT    /api/v1/products/{id}
DELETE /api/v1/products/{id}
```

## Customizations

```http
POST /api/v1/products/{productId}/customizations
GET  /api/v1/products/{productId}/customizations
PUT  /api/v1/products/{productId}/customizations
```

## Payments

```http
POST  /api/v1/payments/orders/{orderId}/initiate
POST  /api/v1/payments/verify
POST  /api/v1/payments/webhook
POST  /api/v1/payments/orders/{orderId}/retry
PATCH /api/v1/payments/orders/{orderId}/attempts/{attemptId}/cancel
```

## Order Reports

```http
POST  /api/v1/orders/{orderId}/reports
GET   /api/v1/users/me/order-reports
GET   /api/v1/order-reports/{reportId}
POST  /api/v1/order-reports/{reportId}/messages

GET   /api/v1/admin/order-reports
GET   /api/v1/admin/order-reports?status=OPEN
GET   /api/v1/admin/order-reports/{reportId}
PATCH /api/v1/admin/order-reports/{reportId}/status
POST  /api/v1/admin/order-reports/{reportId}/messages
POST  /api/v1/admin/order-reports/{reportId}/resolve
```

---

# Frontend

The React frontend communicates with the backend through REST service modules.

Major areas include:

```text
Authentication
Product browsing
Product details
Variants
Customizations
Cart
Checkout
Orders
Order details
Reports
Reviews
User profile
Admin dashboard
```

The product detail flow loads product data, specifications, variants, customizations and reviews.

The checkout flow handles:

```text
Cart
 ↓
Address
 ↓
Order review
 ↓
Payment selection
 ↓
COD / Razorpay
 ↓
Order confirmation
```

---

# Testing

Development testing has covered important API and business workflows, including:

- Authentication
- Refresh-token rotation
- Product creation
- Product variants
- Customizations
- Category filtering
- Payment initiation
- Razorpay verification
- Payment retry
- Razorpay webhooks
- Order reporting
- User/admin report messages
- Report status transitions
- Report resolution
- Product/review frontend integration

The order-report workflow was successfully tested end-to-end:

```text
OPEN
 ↓
IN_REVIEW
 ↓
RESOLVED
```

including user/admin conversation and a refund resolution.

---

# Engineering Decisions

## DTO-based API boundary

JPA entities are not directly used as the public API contract.

DTOs provide:

- Request validation
- Response shaping
- Reduced coupling
- Safer entity evolution
- Clear frontend contracts

## Payment vs PaymentAttempt

Separating them preserves payment history:

```text
Payment
 ├── Attempt #1 → FAILED
 ├── Attempt #2 → FAILED
 └── Attempt #3 → SUCCESS
```

## Redis for Cart

Cart state is temporary and frequently accessed, making Redis suitable for:

- Fast reads/writes
- TTL
- Reduced relational DB traffic
- Temporary session-like commerce state

Orders remain persistent in MySQL.

## Order snapshots

Order items retain important historical fields such as:

```text
productName
variantName
sku
mrp
sellingPrice
customizationPrice
```

so historical orders are not dependent on the current product catalog.

## Hierarchical specifications

Parent category specifications can be resolved for descendant products, reducing repeated category-definition data.

## Controlled state transitions

Order and report states are not freely mutable. Explicit transition validation prevents invalid business states.

---

# Future Improvements

Potential future engineering work:

- Expand automated integration-test coverage
- Testcontainers for MySQL/Redis tests
- Prometheus + Grafana dashboards
- API documentation with OpenAPI/Swagger
- Rate limiting
- More advanced product search/filtering
- Pagination across large collections
- Improved inventory reservation/release workflow
- Async event processing
- CI/CD pipeline
- Docker Compose development environment
- Production deployment configuration
- Centralized logging
- Distributed tracing
- More comprehensive performance testing

These are future improvements and are not claims about the current implementation.

---

# Security Notes

Do not commit credentials or secrets.

For production:

- Protect Actuator endpoints.
- Keep Razorpay secrets server-side.
- Keep JWT signing secrets server-side.
- Use secure HttpOnly refresh-token cookies.
- Restrict administrative APIs with role-based authorization.
- Validate resource ownership for customer operations.
- Verify payment and webhook signatures on the backend.

---

# Project Status

JMSCommerce currently provides a complete working commerce flow:

```text
Authentication
      ↓
Product Catalog
      ↓
Category / Specifications
      ↓
Variants / Customization
      ↓
Redis Cart
      ↓
Checkout
      ↓
COD / Razorpay
      ↓
Orders
      ↓
Reviews
      ↓
Order Reports
      ↓
Admin Resolution
```

The project is intended as a backend-focused, full-stack engineering project demonstrating domain modeling, REST API design, security, persistence, caching, payment integration, state management and operational concerns.

---

# Author

**JMSCommerce**

Full-stack e-commerce engineering project built with Java/Spring Boot and React.
