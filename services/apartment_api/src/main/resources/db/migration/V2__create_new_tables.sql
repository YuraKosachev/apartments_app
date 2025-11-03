CREATE TABLE counties
(
    id   UUID         NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_counties PRIMARY KEY (id)
);

CREATE TABLE states
(
    id   UUID         NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_states PRIMARY KEY (id)
);

ALTER TABLE addresses
    ADD county_id UUID;

ALTER TABLE addresses
    ADD state_id UUID;

ALTER TABLE addresses
    ALTER COLUMN county_id SET NOT NULL;

ALTER TABLE counties
    ADD CONSTRAINT uc_counties_name UNIQUE (name);

ALTER TABLE states
    ADD CONSTRAINT uc_states_name UNIQUE (name);

ALTER TABLE addresses
    ADD CONSTRAINT FK_ADDRESSES_ON_COUNTY FOREIGN KEY (county_id) REFERENCES counties (id);

ALTER TABLE addresses
    ADD CONSTRAINT FK_ADDRESSES_ON_STATE FOREIGN KEY (state_id) REFERENCES states (id);