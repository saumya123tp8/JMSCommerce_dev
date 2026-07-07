CREATE TABLE IF NOT EXISTS category (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          created_at DATETIME NOT NULL,
                          updated_at DATETIME,
                          deleted_at DATETIME,
                          name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS orders (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        created_at DATETIME NOT NULL,
                        updated_at DATETIME,
                        deleted_at DATETIME,
--                      status VARCHAR(255)
                        status TINYINT,
                        current_subtotal DECIMAL(19,2),
                        delivered_at VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS users (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      fname VARCHAR(255),
                      lname VARCHAR(255),
                      age INT,
                      created_at DATETIME NOT NULL,
                      updated_at DATETIME,
                      deleted_at DATETIME
);

CREATE TABLE IF NOT EXISTS product (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         created_at DATETIME NOT NULL,
                         updated_at DATETIME,
                         deleted_at DATETIME,

                         title VARCHAR(255),
                         description VARCHAR(255),
                         price DECIMAL(19,2),
                         image VARCHAR(255),
                         rating INT,

                         category_id BIGINT NOT NULL,

                         CONSTRAINT fk_product_category
                             FOREIGN KEY (category_id)
                                 REFERENCES category(id)
);

CREATE TABLE IF NOT EXISTS order_product (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               created_at DATETIME NOT NULL,
                               current_price DECIMAL(38,2),
                               current_discount DECIMAL(38,2),
                               updated_at DATETIME,
                               deleted_at DATETIME,
                               order_id BIGINT NOT NULL,
                               product_id BIGINT NOT NULL,
                               quantity INT NOT NULL,

                               CONSTRAINT fk_order_product_order
                                   FOREIGN KEY (order_id)
                                       REFERENCES orders(id),

                               CONSTRAINT fk_order_product_product
                                   FOREIGN KEY (product_id)
                                       REFERENCES product(id)
);


CREATE TABLE IF NOT EXISTS order_review (
                                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                             created_at DATETIME NOT NULL,
                                             updated_at DATETIME,
                                             deleted_at DATETIME,

                                             order_id BIGINT NOT NULL,
                                             product_id BIGINT NOT NULL,

                                             comment TEXT,
                                             rating DECIMAL,

                                             CONSTRAINT fk_order_review_order
                                                    FOREIGN KEY (order_id)
                                                        REFERENCES orders(id),

                                             CONSTRAINT fk_order_review_product
                                                     FOREIGN KEY (product_id)
                                                         REFERENCES product(id)
    );