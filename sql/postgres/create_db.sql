--DROP DATABASE IF EXISTS provider;
--CREATE DATABASE provider;
--COMMENT ON DATABASE provider IS 'Java provider project database';

\c provider;

BEGIN;

------------------------------ main schema(public) --------------------------------------------

\set regular_text_regex '''^[[:alpha:]- 0-9]+$'''
\set latin_regex '''^[a-zA-Z0-9_]+$'''
\set name_regex '''^[[:alpha:]\\d-]+$'''

DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    login TEXT NOT NULL UNIQUE
        CONSTRAINT valid_user_login CHECK(login ~* :latin_regex)
        CONSTRAINT long_enough_user_login CHECK(length(login) >= 4),
    role TEXT NOT NULL
        CONSTRAINT valid_user_role CHECK(role ~* :latin_regex),
    name TEXT NOT NULL
        CONSTRAINT valid_user_name CHECK(name ~* :name_regex),
    surname TEXT NOT NULL
        CONSTRAINT valid_user_surname CHECK(surname ~* :name_regex),
    phone TEXT NOT NULL UNIQUE
        CONSTRAINT valid_user_phone CHECK(phone ~* '^([1-9][0-9])?[0-9]{6,10}$'),
    status TEXT NOT NULL
        CONSTRAINT valid_user_status CHECK(status ~* :latin_regex)
);

DROP TABLE IF EXISTS user_passwords;
CREATE TABLE user_passwords(
    user_id BIGINT NOT NULL UNIQUE,
    hash TEXT NOT NULL,           -- Hashed password with salt
    salt TEXT NOT NULL,           -- Salt used with this password
    hash_method TEXT NOT NULL
        CONSTRAINT valid_user_password_hash_method CHECK(hash_method ~* :latin_regex),    -- Hash method identified
    FOREIGN KEY(user_id) REFERENCES users(id)
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS user_accounts CASCADE;
CREATE TABLE user_accounts(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    currency TEXT NOT NULL
        CONSTRAINT valid_user_account_currency CHECK(currency ~* :latin_regex),
    amount NUMERIC(12, 2) DEFAULT 0 NOT NULL
        CONSTRAINT positive_amount CHECK(amount >= 0),
    CONSTRAINT unique_user_id_currency UNIQUE(user_id, currency),
    FOREIGN KEY(user_id) REFERENCES users(id)
        ON DELETE CASCADE
);

-- Service has characteristics(key, value)
-- Tariff plan has services

DROP TABLE IF EXISTS services CASCADE;
CREATE TABLE services(
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
        CONSTRAINT valid_service_name CHECK(name ~* :regular_text_regex)
);

DROP TABLE IF EXISTS service_translations;
CREATE TABLE service_translations(
    service_id INT NOT NULL,
    language TEXT NOT NULL
        CONSTRAINT valid_service_translation_language CHECK (language ~* :latin_regex),
    CONSTRAINT unique_service_language UNIQUE(service_id, language),
    name TEXT NOT NULL
        CONSTRAINT valid_service_name_translation CHECK(name ~* :regular_text_regex),
    FOREIGN KEY(service_id) REFERENCES services(id)
        ON DELETE CASCADE
);

-- key-value pairs describing service characteristics
DROP TABLE IF EXISTS service_characteristics CASCADE;
CREATE TABLE service_characteristics(
    service_id INT NOT NULL,
    name TEXT NOT NULL
        CONSTRAINT valid_tariff_service_characteristic_name CHECK(name ~* :regular_text_regex),
    value TEXT NOT NULL
        CONSTRAINT valid_tariff_service_characteristic_value CHECK(value ~* :regular_text_regex),
    CONSTRAINT unique_service_characteristic UNIQUE(service_id, name, value),
    FOREIGN KEY(service_id) REFERENCES services(id)
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS service_characteristic_translations;
CREATE TABLE service_characteristic_translations(
    service_id INT NOT NULL,
    language TEXT NOT NULL
        CONSTRAINT valid_tariff_service_characteristic_language CHECK (language ~* :latin_regex),
    name TEXT NOT NULL
        CONSTRAINT valid_tariff_service_characteristic_name_translation CHECK(name ~* :regular_text_regex),
    value TEXT NOT NULL
        CONSTRAINT valid_tariff_service_characteristic_value_translation CHECK(value ~* :regular_text_regex),
    CONSTRAINT unique_service_characteristic_translation UNIQUE(service_id, language, name, value),
    FOREIGN KEY(service_id) REFERENCES services(id)
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS tariffs CASCADE;
CREATE TABLE tariffs(
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title TEXT NOT NULL UNIQUE
        CONSTRAINT valid_tariff_title CHECK(title ~* :regular_text_regex),
    status TEXT NOT NULL
        CONSTRAINT valid_tariff_status CHECK(status ~* :latin_regex),
    usd_price NUMERIC(12, 2) NOT NULL
        CONSTRAINT positive_usd_price CHECK(usd_price >= 0)
);

DROP TABLE IF EXISTS tariff_translations;
CREATE TABLE tariff_translations(
    tariff_id INT NOT NULL,
    language TEXT NOT NULL
        CONSTRAINT valid_tariff_translation_language CHECK (language ~* :latin_regex),
    CONSTRAINT unique_tariff_language UNIQUE(tariff_id, language),
    title TEXT NOT NULL
        CONSTRAINT valid_tariff_title_translation CHECK(title ~* :regular_text_regex),
    FOREIGN KEY(tariff_id) REFERENCES tariffs(id)
        ON DELETE CASCADE
);

-- Months and minutes only, for example
DROP TABLE IF EXISTS tariff_durations;
CREATE TABLE tariff_durations(
    tariff_id INT NOT NULL,
    months INT NOT NULL DEFAULT 0
        CONSTRAINT positive_duration_months CHECK(months >= 0),
    minutes BIGINT NOT NULL DEFAULT 0
        CONSTRAINT positive_duration_minutes CHECK(minutes >= 0),
    FOREIGN KEY(tariff_id) REFERENCES tariffs(id)
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS tariff_services CASCADE;
CREATE TABLE tariff_services(
    tariff_id INT NOT NULL,
    service_id INT NOT NULL,
    CONSTRAINT unique_tariff_service UNIQUE(tariff_id, service_id),
    FOREIGN KEY(tariff_id) REFERENCES tariffs(id)
        ON DELETE CASCADE,
    FOREIGN KEY(service_id) REFERENCES services(id)
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS users_tariffs;
CREATE TABLE users_tariffs(
    user_account_id BIGINT NOT NULL,
    tariff_id INT NOT NULL,
    subscribed_at TIMESTAMPTZ NOT NULL DEFAULT current_timestamp,
    FOREIGN KEY(user_account_id) REFERENCES user_accounts(id)
        ON DELETE CASCADE,
    FOREIGN KEY(tariff_id) REFERENCES tariffs(id)
);

END;