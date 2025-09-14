ALTER TABLE t_event
ALTER COLUMN created_by_id TYPE varchar;


ALTER TABLE t_event
    ADD CONSTRAINT fk_event_user
        FOREIGN KEY (created_by_id) REFERENCES m_user(id);

