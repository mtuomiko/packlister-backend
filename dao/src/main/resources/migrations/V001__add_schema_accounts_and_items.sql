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
    public_visibility boolean DEFAULT false,
    account           uuid REFERENCES accounts
);
