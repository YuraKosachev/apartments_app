CREATE TABLE notifications
(
    id         UUID                        NOT NULL,
    subject    VARCHAR(255),
    addressees TEXT,
    body       TEXT                        NOT NULL,
    status     SMALLINT                    NOT NULL,
    type       SMALLINT                    NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    sent_at    TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_notifications PRIMARY KEY (id)
);