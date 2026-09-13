ALTER TABLE users DROP COLUMN emailVerifiedAt;
ALTER TABLE users DROP COLUMN phoneVerifiedAt;


ALTER TABLE users ADD COLUMN email_verified_at DATETIME(6);
ALTER TABLE users ADD COLUMN phone_verified_at DATETIME(6);
