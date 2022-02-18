-- Users
CREATE TABLE "users"
(
  "id"              BIGINT        NOT NULL,
  "alias"           TEXT        NOT NULL,
  "email"           TEXT        NOT NULL,
  "alias_lowercase" TEXT        NOT NULL,
  "email_lowercase" TEXT        NOT NULL,
  "phone"           TEXT        NULL,
  "password"        TEXT        NOT NULL,
  "node_creation_epoch"      BIGINT      NOT Null,
  "created_on"      TIMESTAMPTZ NOT NULL default now(),
  "active"       BOOLEAN NOT NULL
);
ALTER TABLE "users"
  ADD CONSTRAINT "users_id" PRIMARY KEY ("id");
CREATE UNIQUE INDEX "users_login_lowercase" ON "users" ("alias_lowercase");
CREATE UNIQUE INDEX "users_email_lowercase" ON "users" ("email_lowercase");

-- User profiles
CREATE TABLE "user_profiles"
(
  "id"                  BIGINT        NOT NULL,
  "user_id"             BIGINT        NOT NULL,
  "last_name"           TEXT        NOT NULL,
  "first_name"          TEXT        NOT NULL,
  "aka_name"            TEXT        NOT NULL,
  "preferred_contact"   TEXT        NOT NULL,
  "gender"              TEXT        NULL,
  "social_network_accounts"        TEXT        NOT NULL,
  "memo"        TEXT        NOT NULL,
  "created_on"      TIMESTAMPTZ NOT NULL default now(),
  "node_creation_epoch"      BIGINT      NOT Null,
  "updated_on"      TIMESTAMPTZ NOT NULL default now(),
    "node_modificatin_epoch"      BIGINT      NOT Null
);
ALTER TABLE "user_profiles"
  ADD CONSTRAINT "user_profiles_id" PRIMARY KEY ("id");


-- Activation Keys
CREATE TABLE "activation_keys"
(
  "key"         TEXT        NOT NULL,
  "user_id"     BIGINT        NOT NULL,
  "valid_until" BIGINT NOT NULL
);
ALTER TABLE "activation_keys"
  ADD CONSTRAINT "activation_keys_id" PRIMARY KEY ("key","user_id");

-- PASSWORD RESET CODES
CREATE TABLE "password_reset_codes"
(
  "code"          TEXT        NOT NULL,
  "user_id"       BIGINT       NOT NULL,
  "valid_until"   BIGINT       NOT NULL
);
ALTER TABLE "password_reset_codes"
  ADD CONSTRAINT "password_reset_codes_id" PRIMARY KEY ("code","user_id");

-- EMAILS
CREATE TABLE "scheduled_email"
(
  "recipient"        TEXT NOT NULL,
  "subject"          TEXT NOT NULL,
  "content"          TEXT NOT NULL,
  "created_on"       TIMESTAMPTZ NOT NULL,
);
ALTER TABLE "scheduled_email"
  ADD CONSTRAINT "scheduled_email_id" PRIMARY KEY ("created_on");
CREATE UNIQUE INDEX "scheduled_email_key" ON "scheduled_email" ("recipient");

-- SMS
CREATE TABLE "scheduled_sms"
(
  "recipient"       TEXT NOT NULL,
  "subject"         TEXT NOT NULL,
  "content"         TEXT NOT NULL,
  "created_on"       TIMESTAMPTZ NOT NULL,
);
ALTER TABLE "scheduled_sms"
  ADD CONSTRAINT "scheduled_sms_id" PRIMARY KEY ("created_on");
CREATE UNIQUE INDEX "scheduled_sms_key" ON "scheduled_sms" ("recipient");

-- Login Attempts
CREATE TABLE "login_attempts"
(
  "id"        UUID NOT NULL,
  "request_from"   String NOT NULL,
  "count"     INT NOT NULL,
--  "recipient" TEXT NOT NULL,
--  "subject"   TEXT NOT NULL,
--  "content"   TEXT NOT NULL
);
ALTER TABLE "login_attempts"
  ADD CONSTRAINT "login_attempts_id" PRIMARY KEY ("id");
CREATE UNIQUE INDEX "login_attempts_request_from" ON "login_attempts" ("request_from");

-- Account Balances
CREATE TABLE "accounts"
(
  "id"             BIGINT NOT NULL,
  "user_id"        BIGINT NOT NULL,
  "currency"       TEXT NOT NULL,
  "amount"         TEXT NOT NULL
  "account_number" TEXT NOT NULL,

);
ALTER TABLE "accounts"
  ADD CONSTRAINT "accounts_id" PRIMARY KEY ("id");

-- Transaction Entries
CREATE TABLE "transaction_entries"
(
  "from_user_id"   BIGINT NOT NULL,
  "to_user_id"     BIGINT NOT NULL,
  "from_account_number"   TEXT NOT NULL,
  "to_account_number"     TEXT NOT NULL,
  "amount"         BIGINT NOT NULL,
  "currency"       TEXT NOT NULL,
  "status"       TEXT NOT NULL,
  "entered_on"     TIMESTAMPTZ NOT NULL,
);
ALTER TABLE "transaction_entries"
  ADD CONSTRAINT "login_attempts_id" PRIMARY KEY ("id");
CREATE UNIQUE INDEX "users_login_lowercase" ON "users" ("login_lowercase");

-- Executed orders
CREATE TABLE "executions"
(
  "id"               UUID NOT NULL,
  "order_number"     TEXT NOT NULL,
  "executed_on"      TEXT NOT NULL,
  "created_on"       TIMESTAMPTZ NOT NULL,
);
ALTER TABLE "executions"
  ADD CONSTRAINT "executions_id" PRIMARY KEY ("id");
CREATE UNIQUE INDEX "users_login_lowercase" ON "users" ("login_lowercase");
