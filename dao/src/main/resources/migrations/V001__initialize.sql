CREATE SCHEMA IF NOT EXISTS packlister;

CREATE TABLE accounts (
    id            uuid        PRIMARY KEY,
    username      text        UNIQUE NOT NULL,
    email         text        UNIQUE NOT NULL,
    password_hash text        NOT NULL,
    active        boolean     NOT NULL,
    modified_at   timestamptz NOT NULL,
    created_at    timestamptz NOT NULL
);

CREATE TABLE refresh_tokens (
    id          uuid        PRIMARY KEY,
    token       text        NOT NULL,
    family      uuid        UNIQUE NOT NULL,
    expires_at  timestamptz NOT NULL,
    modified_at timestamptz NOT NULL,
    created_at  timestamptz NOT NULL,
    account_id  uuid        REFERENCES accounts
);

CREATE TABLE items (
    id                uuid        PRIMARY KEY,
    name              text,
    description       text,
    weight            integer,
    public_visibility boolean     NOT NULL,
    modified_at       timestamptz NOT NULL,
    created_at        timestamptz NOT NULL,
    account_id        uuid        REFERENCES accounts
);

CREATE TABLE packlists (
    id          uuid        PRIMARY KEY,
    name        text,
    description text,
    content     jsonb       NOT NULL,
    modified_at timestamptz NOT NULL,
    created_at  timestamptz NOT NULL,
    account_id  uuid        REFERENCES accounts
);
