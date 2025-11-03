
ALTER TABLE tasks
    ADD issuer_id UUID;

ALTER TABLE tasks
    ADD predicate TEXT;

ALTER TABLE tasks
    ALTER COLUMN issuer_id SET NOT NULL;

ALTER TABLE tasks
    ALTER COLUMN predicate SET NOT NULL;


CREATE UNIQUE INDEX uidx_task_predicate_issuer ON tasks (predicate, issuer_id);



ALTER TABLE tasks
    DROP COLUMN description;

ALTER TABLE tasks
    DROP COLUMN name;

ALTER TABLE addresses
    ALTER COLUMN city_id DROP NOT NULL;