CREATE TABLE email_verification_token (
                                          id BIGINT NOT NULL AUTO_INCREMENT,

                                          user_id BIGINT NOT NULL,

                                          token_hash VARCHAR(64) NOT NULL,

                                          expires_at TIMESTAMP NOT NULL,

                                          verified_at TIMESTAMP NULL,

                                          created_at TIMESTAMP NOT NULL,

                                          PRIMARY KEY (id),

                                          CONSTRAINT uk_email_verification_token_user
                                              UNIQUE (user_id),

                                          CONSTRAINT uk_email_verification_token_hash
                                              UNIQUE (token_hash),

                                          CONSTRAINT fk_email_verification_token_user
                                              FOREIGN KEY (user_id)
                                                  REFERENCES users(id)
                                                  ON DELETE CASCADE
);

CREATE INDEX idx_email_verification_token_hash
    ON email_verification_token(token_hash);