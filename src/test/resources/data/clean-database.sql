DEALLOCATE ALL;
DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS bank_account;

CREATE TABLE IF NOT EXISTS bank_account
(
    id              SERIAL PRIMARY KEY,
    account_id      VARCHAR(255) NOT NULL UNIQUE,
    first_name      VARCHAR(255) NOT NULL,
    last_name       VARCHAR(255) NOT NULL,
    balance         NUMERIC(10, 2),
    minimum_balance NUMERIC(10, 2),
    active          BOOLEAN,
    created_at      TIMESTAMP DEFAULT NOW(),
    updated_at      TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS transaction
(
    id              SERIAL PRIMARY KEY,
    bank_account_id INT REFERENCES bank_account (id),
    amount          NUMERIC(10, 2),
    type            VARCHAR(255) NOT NULL CHECK (type IN ('DEPOSIT', 'WITHDRAW')),
    created_at      TIMESTAMP DEFAULT NOW()
);