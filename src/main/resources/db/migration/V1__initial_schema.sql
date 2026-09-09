-- Flyway V1: Initial schema
-- MySQL 8.x
-- Generated from the current JPA entity mappings.

CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       created_at DATETIME(6) NOT NULL,
                       updated_at DATETIME(6),
                       deleted_at DATETIME(6),
                       created_by BIGINT,
                       updated_by BIGINT,
                       version BIGINT,

                       name VARCHAR(60) NOT NULL,
                       email VARCHAR(150) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       phone VARCHAR(255),
                       age INT,
                       country_code VARCHAR(3),
                       profile_image VARCHAR(255),
                       email_verified BOOLEAN NOT NULL,
                       phone_verified BOOLEAN NOT NULL,
                       enabled BOOLEAN NOT NULL,
                       account_locked BOOLEAN NOT NULL,
                       last_login_at DATETIME(6),
                       provider VARCHAR(255),

                       PRIMARY KEY (id),
                       UNIQUE KEY uk_users_email (email),
                       KEY idx_user_email (email),
                       KEY idx_user_phone (phone)
);

CREATE TABLE refresh_token (
                               id BIGINT NOT NULL AUTO_INCREMENT,
                               created_at DATETIME(6) NOT NULL,
                               updated_at DATETIME(6),
                               deleted_at DATETIME(6),
                               created_by BIGINT,
                               updated_by BIGINT,
                               version BIGINT,

                               jti VARCHAR(255) NOT NULL,
                               user_id BIGINT NOT NULL,
                               revoked BOOLEAN NOT NULL,
                               expired_at DATETIME(6) NOT NULL,
                               replaced_by_token VARCHAR(255),

                               PRIMARY KEY (id),

                               UNIQUE KEY idx_resfresh_token_jti (jti),
                               KEY idx_resfresh_token_user (user_id),
                               KEY idx_refresh_token_expires (expired_at),

                               CONSTRAINT fk_refresh_token_user
                                   FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE roles (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       created_at DATETIME(6) NOT NULL,
                       updated_at DATETIME(6),
                       deleted_at DATETIME(6),
                       created_by BIGINT,
                       updated_by BIGINT,
                       version BIGINT,

                       name VARCHAR(255),
                       PRIMARY KEY (id),
                       UNIQUE KEY uk_roles_name (name)
);

CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,

                            PRIMARY KEY (user_id, role_id),
                            CONSTRAINT fk_user_roles_user
                                FOREIGN KEY (user_id) REFERENCES users(id),
                            CONSTRAINT fk_user_roles_role
                                FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE addresses (
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           created_at DATETIME(6) NOT NULL,
                           updated_at DATETIME(6),
                           deleted_at DATETIME(6),
                           created_by BIGINT,
                           updated_by BIGINT,
                           version BIGINT,

                           receiver_name VARCHAR(100) NOT NULL,
                           receiver_phone VARCHAR(15) NOT NULL,
                           country_code VARCHAR(5),
                           house_number VARCHAR(100) NOT NULL,
                           apartment VARCHAR(100),
                           street VARCHAR(255) NOT NULL,
                           landmark VARCHAR(255),
                           city VARCHAR(80) NOT NULL,
                           state VARCHAR(80) NOT NULL,
                           country VARCHAR(80) NOT NULL,
                           pincode VARCHAR(10) NOT NULL,
                           type VARCHAR(255) NOT NULL,
                           default_address BOOLEAN NOT NULL,
                           delivery_instructions VARCHAR(255),

                           user_id BIGINT NOT NULL,

                           PRIMARY KEY (id),
                           KEY idx_address_user (user_id),
                           KEY idx_address_pincode (pincode),
                           CONSTRAINT fk_address_user
                               FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE categories (
                            id BIGINT NOT NULL AUTO_INCREMENT,
                            created_at DATETIME(6) NOT NULL,
                            updated_at DATETIME(6),
                            deleted_at DATETIME(6),
                            created_by BIGINT,
                            updated_by BIGINT,
                            version BIGINT,

                            name VARCHAR(30) NOT NULL,
                            slug VARCHAR(180) NOT NULL,
                            description VARCHAR(500),
                            status VARCHAR(20) NOT NULL,
                            parent_category_id BIGINT,
                            level INT,

                            PRIMARY KEY (id),
                            UNIQUE KEY uk_category_slug (slug),
                            KEY idx_category_slug (slug),
                            KEY idx_category_status (status),
                            CONSTRAINT fk_category_parent
                                FOREIGN KEY (parent_category_id) REFERENCES categories(id)
);

CREATE TABLE brand (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       created_at DATETIME(6) NOT NULL,
                       updated_at DATETIME(6),
                       deleted_at DATETIME(6),
                       created_by BIGINT,
                       updated_by BIGINT,
                       version BIGINT,

                       name VARCHAR(255),
                       stablish_date DATETIME(6),
                       description VARCHAR(255),
                       logo VARCHAR(255),

                       PRIMARY KEY (id)
);

CREATE TABLE specification_definitions (
                                           id BIGINT NOT NULL AUTO_INCREMENT,
                                           created_at DATETIME(6) NOT NULL,
                                           updated_at DATETIME(6),
                                           deleted_at DATETIME(6),
                                           created_by BIGINT,
                                           updated_by BIGINT,
                                           version BIGINT,

                                           name VARCHAR(255),
                                           display_name VARCHAR(255),
                                           description VARCHAR(255),
                                           data_type VARCHAR(255),
                                           unit VARCHAR(255),
                                           required BOOLEAN,
                                           filterable BOOLEAN,
                                           searchable BOOLEAN,
                                           display_order INT,
                                           placeholder VARCHAR(255),
                                           default_value VARCHAR(255),
                                           definition_type VARCHAR(255) NOT NULL,
                                           category_id BIGINT NOT NULL,

                                           PRIMARY KEY (id),
                                           CONSTRAINT fk_specification_definition_category
                                               FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE product (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         created_at DATETIME(6) NOT NULL,
                         updated_at DATETIME(6),
                         deleted_at DATETIME(6),
                         created_by BIGINT,
                         updated_by BIGINT,
                         version BIGINT,

                         name VARCHAR(255),
                         currency VARCHAR(255),
                         selling_price DECIMAL(19,2),
                         mrp DECIMAL(19,2),
                         primary_image VARCHAR(255),
                         slug VARCHAR(255),
                         short_description VARCHAR(255),
                         description TEXT,
                         category_id BIGINT NOT NULL,
                         inventory_type VARCHAR(255) NOT NULL,
                         rating DOUBLE,
                         rating_count INT,
                         review_count INT,
                         status VARCHAR(255),
                         brand_id BIGINT,

                         PRIMARY KEY (id),
                         CONSTRAINT fk_product_category
                             FOREIGN KEY (category_id) REFERENCES categories(id),
                         CONSTRAINT fk_product_brand
                             FOREIGN KEY (brand_id) REFERENCES brand(id)
);

CREATE TABLE product_variant (
                                 id BIGINT NOT NULL AUTO_INCREMENT,
                                 created_at DATETIME(6) NOT NULL,
                                 updated_at DATETIME(6),
                                 deleted_at DATETIME(6),
                                 created_by BIGINT,
                                 updated_by BIGINT,
                                 version BIGINT,

                                 product_id BIGINT NOT NULL,
                                 display_name VARCHAR(255) NOT NULL,
                                 mrp DECIMAL(19,2) NOT NULL,
                                 selling_price DECIMAL(19,2) NOT NULL,
                                 stock INT,
                                 sku VARCHAR(255),
                                 barcode VARCHAR(255),
                                 active BOOLEAN,

                                 PRIMARY KEY (id),
                                 UNIQUE KEY uk_product_variant_sku (sku),
                                 UNIQUE KEY uk_product_variant_barcode (barcode),
                                 CONSTRAINT fk_product_variant_product
                                     FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE variant_attribute (
                                   id BIGINT NOT NULL AUTO_INCREMENT,
                                   created_at DATETIME(6) NOT NULL,
                                   updated_at DATETIME(6),
                                   deleted_at DATETIME(6),
                                   created_by BIGINT,
                                   updated_by BIGINT,
                                   version BIGINT,

                                   variant_id BIGINT NOT NULL,
                                   specification_definition_id BIGINT NOT NULL,
                                   value VARCHAR(255) NOT NULL,

                                   PRIMARY KEY (id),
                                   CONSTRAINT fk_variant_attribute_variant
                                       FOREIGN KEY (variant_id) REFERENCES product_variant(id),
                                   CONSTRAINT fk_variant_attribute_definition
                                       FOREIGN KEY (specification_definition_id) REFERENCES specification_definitions(id)
);

CREATE TABLE product_specification (
                                       id BIGINT NOT NULL AUTO_INCREMENT,
                                       created_at DATETIME(6) NOT NULL,
                                       updated_at DATETIME(6),
                                       deleted_at DATETIME(6),
                                       created_by BIGINT,
                                       updated_by BIGINT,
                                       version BIGINT,

                                       product_id BIGINT NOT NULL,
                                       specification_definition_id BIGINT NOT NULL,
                                       value TEXT NOT NULL,

                                       PRIMARY KEY (id),
                                       UNIQUE KEY uk_product_specification (product_id, specification_definition_id),
                                       CONSTRAINT fk_product_specification_product
                                           FOREIGN KEY (product_id) REFERENCES product(id),
                                       CONSTRAINT fk_product_specification_definition
                                           FOREIGN KEY (specification_definition_id) REFERENCES specification_definitions(id)
);

CREATE TABLE customization_group (
                                     id BIGINT NOT NULL AUTO_INCREMENT,
                                     created_at DATETIME(6) NOT NULL,
                                     updated_at DATETIME(6),
                                     deleted_at DATETIME(6),
                                     created_by BIGINT,
                                     updated_by BIGINT,
                                     version BIGINT,

                                     product_id BIGINT NOT NULL,
                                     name VARCHAR(255) NOT NULL,
                                     selection_type VARCHAR(255) NOT NULL,
                                     min_selection INT NOT NULL,
                                     max_selection INT NOT NULL,
                                     display_order INT,
                                     active BOOLEAN,
                                     required BOOLEAN NOT NULL,

                                     PRIMARY KEY (id),
                                     CONSTRAINT fk_customization_group_product
                                         FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE customization_option (
                                      id BIGINT NOT NULL AUTO_INCREMENT,
                                      created_at DATETIME(6) NOT NULL,
                                      updated_at DATETIME(6),
                                      deleted_at DATETIME(6),
                                      created_by BIGINT,
                                      updated_by BIGINT,
                                      version BIGINT,

                                      customization_group_id BIGINT NOT NULL,
                                      name VARCHAR(255) NOT NULL,
                                      adjustment_type VARCHAR(255) NOT NULL,
                                      adjustment_value DECIMAL(19,2) NOT NULL,
                                      display_order INT,
                                      active BOOLEAN,

                                      PRIMARY KEY (id),
                                      CONSTRAINT fk_customization_option_group
                                          FOREIGN KEY (customization_group_id) REFERENCES customization_group(id)
);

CREATE TABLE order_delivery_address (
                                        id BIGINT NOT NULL AUTO_INCREMENT,
                                        created_at DATETIME(6) NOT NULL,
                                        updated_at DATETIME(6),
                                        deleted_at DATETIME(6),
                                        created_by BIGINT,
                                        updated_by BIGINT,
                                        version BIGINT,

                                        receiver_name VARCHAR(255) NOT NULL,
                                        receiver_phone VARCHAR(255) NOT NULL,
                                        country_code VARCHAR(255),
                                        house_number VARCHAR(255) NOT NULL,
                                        apartment VARCHAR(255),
                                        street VARCHAR(255) NOT NULL,
                                        landmark VARCHAR(255),
                                        city VARCHAR(255) NOT NULL,
                                        state VARCHAR(255) NOT NULL,
                                        country VARCHAR(255) NOT NULL,
                                        pincode VARCHAR(255) NOT NULL,
                                        type VARCHAR(255) NOT NULL,
                                        delivery_instructions VARCHAR(500),

                                        PRIMARY KEY (id)
);

CREATE TABLE orders (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        created_at DATETIME(6) NOT NULL,
                        updated_at DATETIME(6),
                        deleted_at DATETIME(6),
                        created_by BIGINT,
                        updated_by BIGINT,
                        version BIGINT,

                        status VARCHAR(255) NOT NULL,
                        payment_status VARCHAR(255) NOT NULL,
                        subtotal DECIMAL(19,2) NOT NULL,
                        discount DECIMAL(19,2) NOT NULL,
                        tax DECIMAL(19,2) NOT NULL,
                        delivery_charge DECIMAL(19,2) NOT NULL,
                        grand_total DECIMAL(19,2) NOT NULL,
                        currency VARCHAR(255) NOT NULL,
                        delivery_address_id BIGINT NOT NULL,
                        user_id BIGINT NOT NULL,
                        order_number VARCHAR(255) NOT NULL,

                        PRIMARY KEY (id),
                        UNIQUE KEY uk_orders_order_number (order_number),
                        UNIQUE KEY uk_orders_delivery_address (delivery_address_id),
                        CONSTRAINT fk_orders_delivery_address
                            FOREIGN KEY (delivery_address_id) REFERENCES order_delivery_address(id),
                        CONSTRAINT fk_orders_user
                            FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE order_item (
                            id BIGINT NOT NULL AUTO_INCREMENT,
                            created_at DATETIME(6) NOT NULL,
                            updated_at DATETIME(6),
                            deleted_at DATETIME(6),
                            created_by BIGINT,
                            updated_by BIGINT,
                            version BIGINT,

                            order_id BIGINT NOT NULL,
                            variant_id BIGINT NOT NULL,
                            quantity INT NOT NULL,
                            mrp DECIMAL(19,2) NOT NULL,
                            selling_price DECIMAL(19,2) NOT NULL,
                            customization_price DECIMAL(19,2) NOT NULL,
                            total_price DECIMAL(19,2) NOT NULL,
                            sku VARCHAR(255) NOT NULL,
                            variant_name VARCHAR(255) NOT NULL,
                            product_name VARCHAR(255),
                            inventory_reserved BOOLEAN,

                            PRIMARY KEY (id),
                            CONSTRAINT fk_order_item_order
                                FOREIGN KEY (order_id) REFERENCES orders(id),
                            CONSTRAINT fk_order_item_variant
                                FOREIGN KEY (variant_id) REFERENCES product_variant(id)
);

CREATE TABLE order_item_customization (
                                          id BIGINT NOT NULL AUTO_INCREMENT,
                                          created_at DATETIME(6) NOT NULL,
                                          updated_at DATETIME(6),
                                          deleted_at DATETIME(6),
                                          created_by BIGINT,
                                          updated_by BIGINT,
                                          version BIGINT,

                                          order_item_id BIGINT NOT NULL,
                                          customization_option_id BIGINT NOT NULL,
                                          name VARCHAR(255) NOT NULL,
                                          price_adjustment DECIMAL(19,2) NOT NULL,

                                          PRIMARY KEY (id),
                                          CONSTRAINT fk_order_item_customization_item
                                              FOREIGN KEY (order_item_id) REFERENCES order_item(id)
);

CREATE TABLE payments (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          created_at DATETIME(6) NOT NULL,
                          updated_at DATETIME(6),
                          deleted_at DATETIME(6),
                          created_by BIGINT,
                          updated_by BIGINT,
                          version BIGINT,

                          order_id BIGINT NOT NULL,
                          method VARCHAR(255) NOT NULL,
                          status VARCHAR(255) NOT NULL,
                          amount DECIMAL(19,2) NOT NULL,
                          currency VARCHAR(255) NOT NULL,

                          PRIMARY KEY (id),
                          UNIQUE KEY uk_payments_order (order_id),
                          CONSTRAINT fk_payments_order
                              FOREIGN KEY (order_id) REFERENCES orders(id)
);

CREATE TABLE payment_attempts (
                                  id BIGINT NOT NULL AUTO_INCREMENT,
                                  created_at DATETIME(6) NOT NULL,
                                  updated_at DATETIME(6),
                                  deleted_at DATETIME(6),
                                  created_by BIGINT,
                                  updated_by BIGINT,
                                  version BIGINT,

                                  payment_id BIGINT NOT NULL,
                                  status VARCHAR(255) NOT NULL,
                                  amount DECIMAL(19,2) NOT NULL,
                                  currency VARCHAR(10) NOT NULL,
                                  razorpay_order_id VARCHAR(255),
                                  razorpay_payment_id VARCHAR(255),
                                  razorpay_signature VARCHAR(255),
                                  initiated_at DATETIME(6),
                                  paid_at DATETIME(6),
                                  failed_at DATETIME(6),
                                  failure_code VARCHAR(100),
                                  failure_message VARCHAR(1000),

                                  PRIMARY KEY (id),
                                  UNIQUE KEY uk_payment_attempt_razorpay_order (razorpay_order_id),
                                  UNIQUE KEY uk_payment_attempt_razorpay_payment (razorpay_payment_id),
                                  KEY idx_attempt_payment (payment_id),
                                  KEY idx_attempt_razorpay_order (razorpay_order_id),
                                  KEY idx_attempt_razorpay_payment (razorpay_payment_id),
                                  CONSTRAINT fk_payment_attempt_payment
                                      FOREIGN KEY (payment_id) REFERENCES payments(id)
);

CREATE TABLE order_reports (
                               id BIGINT NOT NULL AUTO_INCREMENT,
                               created_at DATETIME(6) NOT NULL,
                               updated_at DATETIME(6),
                               deleted_at DATETIME(6),
                               created_by BIGINT,
                               updated_by BIGINT,
                               version BIGINT,

                               order_id BIGINT NOT NULL,
                               reason VARCHAR(40) NOT NULL,
                               description TEXT,
                               status VARCHAR(20) NOT NULL,

                               PRIMARY KEY (id),
                               KEY idx_order_report_order (order_id),
                               KEY idx_order_report_status (status),
                               CONSTRAINT fk_order_report_order
                                   FOREIGN KEY (order_id) REFERENCES orders(id)
);

CREATE TABLE order_report_messages (
                                       id BIGINT NOT NULL AUTO_INCREMENT,
                                       created_at DATETIME(6) NOT NULL,
                                       updated_at DATETIME(6),
                                       deleted_at DATETIME(6),
                                       created_by BIGINT,
                                       updated_by BIGINT,
                                       version BIGINT,

                                       report_id BIGINT NOT NULL,
                                       sender_id BIGINT NOT NULL,
                                       sender_type VARCHAR(10) NOT NULL,
                                       message TEXT NOT NULL,

                                       PRIMARY KEY (id),
                                       KEY idx_report_message_report (report_id),
                                       CONSTRAINT fk_report_message_report
                                           FOREIGN KEY (report_id) REFERENCES order_reports(id),
                                       CONSTRAINT fk_report_message_sender
                                           FOREIGN KEY (sender_id) REFERENCES users(id)
);

CREATE TABLE order_report_resolutions (
                                          id BIGINT NOT NULL AUTO_INCREMENT,
                                          created_at DATETIME(6) NOT NULL,
                                          updated_at DATETIME(6),
                                          deleted_at DATETIME(6),
                                          created_by BIGINT,
                                          updated_by BIGINT,
                                          version BIGINT,

                                          report_id BIGINT NOT NULL,
                                          type VARCHAR(30) NOT NULL,
                                          message TEXT NOT NULL,
                                          resolved_at DATETIME(6) NOT NULL,
                                          resolved_by BIGINT NOT NULL,

                                          PRIMARY KEY (id),
                                          UNIQUE KEY uk_order_report_resolution_report (report_id),
                                          CONSTRAINT fk_report_resolution_report
                                              FOREIGN KEY (report_id) REFERENCES order_reports(id),
                                          CONSTRAINT fk_report_resolution_user
                                              FOREIGN KEY (resolved_by) REFERENCES users(id)
);

CREATE TABLE review (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        created_at DATETIME(6) NOT NULL,
                        updated_at DATETIME(6),
                        deleted_at DATETIME(6),
                        created_by BIGINT,
                        updated_by BIGINT,
                        version BIGINT,

                        rating INT NOT NULL,
                        title VARCHAR(150) NOT NULL,
                        review_text TEXT,
                        verified_purchase BOOLEAN NOT NULL,
                        helpful_count INT NOT NULL,
                        status VARCHAR(255),
                        order_item_id BIGINT NOT NULL,

                        PRIMARY KEY (id),
                        UNIQUE KEY uk_review_order_item (order_item_id),
                        CONSTRAINT fk_review_order_item
                            FOREIGN KEY (order_item_id) REFERENCES order_item(id)
);
