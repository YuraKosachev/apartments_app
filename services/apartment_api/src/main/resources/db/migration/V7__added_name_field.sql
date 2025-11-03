ALTER TABLE tasks
    ADD name VARCHAR(255) NOT NULL;

ALTER TABLE tasks
    ADD CONSTRAINT uc_tasks_name UNIQUE (name);
