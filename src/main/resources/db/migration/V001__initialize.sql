CREATE SCHEMA IF NOT EXISTS packlister;

CREATE TABLE accounts (
    id            uuid        PRIMARY KEY,
    username      text        NOT NULL,
    email         text        NOT NULL,
    password_hash text        NOT NULL,
    active        boolean     NOT NULL,
    modified_at   timestamptz NOT NULL,
    created_at    timestamptz NOT NULL
);
