CREATE TABLE password_reset_token (
                                      id BIGINT NOT NULL AUTO_INCREMENT,

                                      user_id BIGINT NOT NULL,
                                      token_hash VARCHAR(64) NOT NULL,
                                      expires_at DATETIME(6) NOT NULL,
                                      used_at DATETIME(6),
                                      created_at DATETIME(6) NOT NULL,

                                      PRIMARY KEY (id),

                                      UNIQUE KEY uk_password_reset_token_user (user_id),
                                      UNIQUE KEY uk_password_reset_token_hash (token_hash),

                                      KEY idx_password_reset_token_hash (token_hash),

                                      CONSTRAINT fk_password_reset_token_user
                                          FOREIGN KEY (user_id)
                                              REFERENCES users(id)
);