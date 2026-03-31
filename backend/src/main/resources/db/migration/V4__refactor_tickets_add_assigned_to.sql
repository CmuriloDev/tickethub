ALTER TABLE tickets
    RENAME COLUMN user_id TO created_by_id;

ALTER TABLE tickets
    ADD COLUMN assigned_to_id UUID REFERENCES users(id) ON DELETE SET NULL;

DROP INDEX IF EXISTS idx_tickets_user_id;
DROP INDEX IF EXISTS idx_tickets_user_status;
DROP INDEX IF EXISTS idx_tickets_user_priority;

CREATE INDEX idx_tickets_created_by ON tickets(created_by_id);
CREATE INDEX idx_tickets_assigned_to ON tickets(assigned_to_id);
CREATE INDEX idx_tickets_created_by_status ON tickets(created_by_id, status);
