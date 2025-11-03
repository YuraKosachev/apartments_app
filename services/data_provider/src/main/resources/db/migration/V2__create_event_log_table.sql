CREATE TABLE event_logs
(
    id         UUID                        NOT NULL,
    type       SMALLINT,
    content    TEXT                        NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    sent_at    TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_event_logs PRIMARY KEY (id)
);