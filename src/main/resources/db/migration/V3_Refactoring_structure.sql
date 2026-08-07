CREATE TABLE customization_group(

                                    id UUID PRIMARY KEY,

                                    product_id UUID NOT NULL,

                                    name VARCHAR(255) NOT NULL,

                                    selection_type VARCHAR(20) NOT NULL,

                                    min_selection INTEGER NOT NULL,

                                    max_selection INTEGER NOT NULL,

                                    display_order INTEGER DEFAULT 0,

                                    active BOOLEAN DEFAULT TRUE,

                                    created_at TIMESTAMP,

                                    updated_at TIMESTAMP,

                                    deleted_at TIMESTAMP,

                                    CONSTRAINT fk_customization_group_product
                                        FOREIGN KEY(product_id)
                                            REFERENCES product(id)
);

CREATE TABLE customization_option(

                                     id UUID PRIMARY KEY,

                                     customization_group_id UUID NOT NULL,

                                     name VARCHAR(255) NOT NULL,

                                     price_adjustment DECIMAL(12,2) DEFAULT 0,

                                     display_order INTEGER DEFAULT 0,

                                     active BOOLEAN DEFAULT TRUE,

                                     created_at TIMESTAMP,

                                     updated_at TIMESTAMP,

                                     deleted_at TIMESTAMP,

                                     CONSTRAINT fk_customization_option_group
                                         FOREIGN KEY(customization_group_id)
                                             REFERENCES customization_group(id)
);

