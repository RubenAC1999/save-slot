CREATE TABLE users (
    id          UUID         PRIMARY KEY,
    email       VARCHAR(150) UNIQUE NOT NULL,
    username    VARCHAR(100) UNIQUE NOT NULL,
    password    VARCHAR(255) NOT NULL,
    created_at  TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);
