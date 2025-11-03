-- Migration V2: Criar tabela de eventos
-- Armazena os eventos de amigo secreto criados pelos usuários

CREATE TABLE events (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id NUMBER NOT NULL,
    name VARCHAR2(100) NOT NULL,
    description VARCHAR2(500),
    event_date DATE NOT NULL,
    status VARCHAR2(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    
    -- Foreign Keys
    CONSTRAINT fk_events_user FOREIGN KEY (user_id) 
        REFERENCES users(id) ON DELETE CASCADE,
    
    -- Constraints
    CONSTRAINT chk_events_status CHECK (status IN ('PENDING', 'DRAWN', 'COMPLETED', 'CANCELLED'))
);

-- Índices para melhorar performance
CREATE INDEX idx_events_user_id ON events(user_id);
CREATE INDEX idx_events_status ON events(status);
CREATE INDEX idx_events_event_date ON events(event_date);
CREATE INDEX idx_events_user_status ON events(user_id, status);

-- Comentários nas colunas
COMMENT ON TABLE events IS 'Tabela de eventos de amigo secreto';
COMMENT ON COLUMN events.id IS 'ID único do evento';
COMMENT ON COLUMN events.user_id IS 'ID do usuário criador do evento';
COMMENT ON COLUMN events.name IS 'Nome do evento';
COMMENT ON COLUMN events.description IS 'Descrição opcional do evento';
COMMENT ON COLUMN events.event_date IS 'Data prevista para o evento';
COMMENT ON COLUMN events.status IS 'Status do evento: PENDING, DRAWN, COMPLETED, CANCELLED';
COMMENT ON COLUMN events.created_at IS 'Data de criação do registro';
COMMENT ON COLUMN events.updated_at IS 'Data da última atualização';

