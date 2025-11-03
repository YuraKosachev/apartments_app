ALTER TABLE tasks
    DROP COLUMN issuer_id;

ALTER TABLE tasks
    ADD issuer_id VARCHAR(255) NOT NULL;

ALTER TABLE task_apartments
DROP CONSTRAINT fk_task_apartments_on_task,
ADD CONSTRAINT fk_task_apartments_on_task
FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE;
