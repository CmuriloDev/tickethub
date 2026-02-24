CREATE TABLE tickets (
                         id UUID PRIMARY KEY,
                         user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                         title VARCHAR(160) NOT NULL,
                         description TEXT,
                         status VARCHAR(30) NOT NULL,
                         priority VARCHAR(30) NOT NULL,
                         created_at TIMESTAMP NOT NULL DEFAULT now(),
                         updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_tickets_user_id ON tickets(user_id);
CREATE INDEX idx_tickets_user_status ON tickets(user_id, status);
CREATE INDEX idx_tickets_user_priority ON tickets(user_id, priority);
