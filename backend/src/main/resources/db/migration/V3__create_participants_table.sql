-- Migration V3: Criar tabela de participantes
-- Armazena os participantes de cada evento de amigo secreto

CREATE TABLE participants (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id NUMBER NOT NULL,
    name VARCHAR2(100) NOT NULL,
    email VARCHAR2(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    
    -- Foreign Keys
    CONSTRAINT fk_participants_event FOREIGN KEY (event_id) 
        REFERENCES events(id) ON DELETE CASCADE,
    
    -- Constraints
    CONSTRAINT uk_participants_event_email UNIQUE (event_id, email)
);

-- Índices para melhorar performance
CREATE INDEX idx_participants_event_id ON participants(event_id);
CREATE INDEX idx_participants_email ON participants(email);
CREATE INDEX idx_participants_name ON participants(name);

-- Comentários nas colunas
COMMENT ON TABLE participants IS 'Tabela de participantes dos eventos';
COMMENT ON COLUMN participants.id IS 'ID único do participante';
COMMENT ON COLUMN participants.event_id IS 'ID do evento ao qual o participante pertence';
COMMENT ON COLUMN participants.name IS 'Nome do participante';
COMMENT ON COLUMN participants.email IS 'Email do participante';
COMMENT ON COLUMN participants.created_at IS 'Data de criação do registro';
COMMENT ON COLUMN participants.updated_at IS 'Data da última atualização';

