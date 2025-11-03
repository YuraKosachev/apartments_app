CREATE TABLE addresses
(
    id             UUID             NOT NULL,
    postcode_id    UUID,
    city_id        UUID             NOT NULL,
    country_id     UUID             NOT NULL,
    district_id    UUID,
    street_id      UUID,
    latitude       DOUBLE PRECISION NOT NULL,
    longitude      DOUBLE PRECISION NOT NULL,
    box_longitude1 DOUBLE PRECISION,
    box_latitude1  DOUBLE PRECISION,
    box_longitude2 DOUBLE PRECISION,
    box_latitude2  DOUBLE PRECISION,
    house_number   VARCHAR(255),
    CONSTRAINT pk_addresses PRIMARY KEY (id)
);

CREATE TABLE apartments
(
    id               UUID                        NOT NULL,
    apartment_type   SMALLINT                    NOT NULL,
    source_id        UUID                        NOT NULL,
    address_id       UUID,
    photo            VARCHAR(255),
    url              VARCHAR(255)                NOT NULL,
    resale           BOOLEAN,
    number_of_rooms  INTEGER,
    floor            INTEGER,
    number_of_floors INTEGER,
    total            DOUBLE PRECISION,
    living           DOUBLE PRECISION,
    kitchen          DOUBLE PRECISION,
    seller_type      VARCHAR(255),
    rent_type        VARCHAR(255),
    owner            BOOLEAN,
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_time_up     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    deleted_at       TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_apartments PRIMARY KEY (id)
);

CREATE TABLE cities
(
    id   UUID         NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_cities PRIMARY KEY (id)
);

CREATE TABLE countries
(
    id   UUID         NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_countries PRIMARY KEY (id)
);

CREATE TABLE districs
(
    id   UUID         NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_districs PRIMARY KEY (id)
);

CREATE TABLE posts
(
    id       UUID    NOT NULL,
    postcode INTEGER NOT NULL,
    CONSTRAINT pk_posts PRIMARY KEY (id)
);

CREATE TABLE prices
(
    id           UUID                        NOT NULL,
    amount       DOUBLE PRECISION            NOT NULL,
    apartment_id UUID                        NOT NULL,
    currency     VARCHAR(255)                NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_prices PRIMARY KEY (id)
);

CREATE TABLE streets
(
    id   UUID         NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_streets PRIMARY KEY (id)
);

CREATE TABLE task_apartments
(
    created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    task_id      UUID                        NOT NULL,
    apartment_id UUID                        NOT NULL,
    CONSTRAINT pk_task_apartments PRIMARY KEY (task_id, apartment_id)
);

CREATE TABLE tasks
(
    id          UUID                        NOT NULL,
    name        VARCHAR(255)                NOT NULL,
    description VARCHAR(255),
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_tasks PRIMARY KEY (id)
);

ALTER TABLE cities
    ADD CONSTRAINT uc_cities_name UNIQUE (name);

ALTER TABLE countries
    ADD CONSTRAINT uc_countries_name UNIQUE (name);

ALTER TABLE districs
    ADD CONSTRAINT uc_districs_name UNIQUE (name);

ALTER TABLE posts
    ADD CONSTRAINT uc_posts_postcode UNIQUE (postcode);

ALTER TABLE streets
    ADD CONSTRAINT uc_streets_name UNIQUE (name);

ALTER TABLE tasks
    ADD CONSTRAINT uc_tasks_name UNIQUE (name);

ALTER TABLE addresses
    ADD CONSTRAINT FK_ADDRESSES_ON_CITY FOREIGN KEY (city_id) REFERENCES cities (id);

ALTER TABLE addresses
    ADD CONSTRAINT FK_ADDRESSES_ON_COUNTRY FOREIGN KEY (country_id) REFERENCES countries (id);

ALTER TABLE addresses
    ADD CONSTRAINT FK_ADDRESSES_ON_DISTRICT FOREIGN KEY (district_id) REFERENCES districs (id);

ALTER TABLE addresses
    ADD CONSTRAINT FK_ADDRESSES_ON_POSTCODE FOREIGN KEY (postcode_id) REFERENCES posts (id);

ALTER TABLE addresses
    ADD CONSTRAINT FK_ADDRESSES_ON_STREET FOREIGN KEY (street_id) REFERENCES streets (id);

ALTER TABLE apartments
    ADD CONSTRAINT FK_APARTMENTS_ON_ADDRESS FOREIGN KEY (address_id) REFERENCES addresses (id);

ALTER TABLE prices
    ADD CONSTRAINT FK_PRICES_ON_APARTMENT FOREIGN KEY (apartment_id) REFERENCES apartments (id);

ALTER TABLE task_apartments
    ADD CONSTRAINT FK_TASK_APARTMENTS_ON_APARTMENT FOREIGN KEY (apartment_id) REFERENCES apartments (id);

ALTER TABLE task_apartments
    ADD CONSTRAINT FK_TASK_APARTMENTS_ON_TASK FOREIGN KEY (task_id) REFERENCES tasks (id);