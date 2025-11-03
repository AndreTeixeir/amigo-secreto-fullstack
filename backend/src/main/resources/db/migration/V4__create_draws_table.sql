-- Migration V4: Criar tabela de sorteios
-- Armazena o resultado dos sorteios de amigo secreto

CREATE TABLE draws (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id NUMBER NOT NULL,
    giver_id NUMBER NOT NULL,
    receiver_id NUMBER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    
    -- Foreign Keys
    CONSTRAINT fk_draws_event FOREIGN KEY (event_id) 
        REFERENCES events(id) ON DELETE CASCADE,
    CONSTRAINT fk_draws_giver FOREIGN KEY (giver_id) 
        REFERENCES participants(id) ON DELETE CASCADE,
    CONSTRAINT fk_draws_receiver FOREIGN KEY (receiver_id) 
        REFERENCES participants(id) ON DELETE CASCADE,
    
    -- Constraints
    CONSTRAINT chk_draws_different_participants CHECK (giver_id != receiver_id),
    CONSTRAINT uk_draws_event_giver UNIQUE (event_id, giver_id),
    CONSTRAINT uk_draws_event_receiver UNIQUE (event_id, receiver_id)
);

-- Índices para melhorar performance
CREATE INDEX idx_draws_event_id ON draws(event_id);
CREATE INDEX idx_draws_giver_id ON draws(giver_id);
CREATE INDEX idx_draws_receiver_id ON draws(receiver_id);

-- Comentários nas colunas
COMMENT ON TABLE draws IS 'Tabela de resultados dos sorteios';
COMMENT ON COLUMN draws.id IS 'ID único do sorteio';
COMMENT ON COLUMN draws.event_id IS 'ID do evento do sorteio';
COMMENT ON COLUMN draws.giver_id IS 'ID do participante que vai dar o presente';
COMMENT ON COLUMN draws.receiver_id IS 'ID do participante que vai receber o presente';
COMMENT ON COLUMN draws.created_at IS 'Data de criação do sorteio';

