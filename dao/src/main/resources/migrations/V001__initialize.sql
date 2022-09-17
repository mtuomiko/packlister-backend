CREATE SCHEMA IF NOT EXISTS packlister;

CREATE TABLE IF NOT EXISTS accounts (
    id            uuid PRIMARY KEY,
    username      text UNIQUE NOT NULL,
    email         text UNIQUE NOT NULL,
    password_hash text NOT NULL
);

CREATE TABLE IF NOT EXISTS items (
    id                uuid PRIMARY KEY,
    name              text,
    description       text,
    weight            integer,
    public_visibility boolean NOT NULL,
    account           uuid REFERENCES accounts
);

CREATE TABLE IF NOT EXISTS packlists (
    id          uuid PRIMARY KEY,
    name        text,
    description text,
    content     jsonb NOT NULL,
    account     uuid REFERENCES accounts
);
