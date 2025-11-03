package com.amigosecreto.repository;

import com.amigosecreto.model.Draw;
import com.amigosecreto.model.Event;
import com.amigosecreto.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de banco de dados da entidade Draw.
 */
@Repository
public interface DrawRepository extends JpaRepository<Draw, Long> {

    /**
     * Busca todos os sorteios de um evento.
     *
     * @param event Evento
     * @return Lista de sorteios do evento
     */
    List<Draw> findByEvent(Event event);

    /**
     * Busca sorteio onde um participante é o doador.
     *
     * @param giver Participante doador
     * @return Optional contendo o sorteio se encontrado
     */
    Optional<Draw> findByGiver(Participant giver);

    /**
     * Busca sorteio onde um participante é o receptor.
     *
     * @param receiver Participante receptor
     * @return Optional contendo o sorteio se encontrado
     */
    Optional<Draw> findByReceiver(Participant receiver);

    /**
     * Busca sorteio de um participante específico em um evento.
     *
     * @param event Evento
     * @param giver Participante doador
     * @return Optional contendo o sorteio se encontrado
     */
    Optional<Draw> findByEventAndGiver(Event event, Participant giver);

    /**
     * Verifica se já existe sorteio para um evento.
     *
     * @param event Evento
     * @return true se existir, false caso contrário
     */
    boolean existsByEvent(Event event);

    /**
     * Conta quantos sorteios um evento possui.
     *
     * @param event Evento
     * @return Número de sorteios
     */
    long countByEvent(Event event);

    /**
     * Deleta todos os sorteios de um evento.
     *
     * @param event Evento
     */
    void deleteByEvent(Event event);

    /**
     * Busca todos os sorteios de um evento com participantes carregados.
     *
     * @param event Evento
     * @return Lista de sorteios com participantes
     */
    @Query("SELECT d FROM Draw d JOIN FETCH d.giver JOIN FETCH d.receiver WHERE d.event = :event")
    List<Draw> findByEventWithParticipants(@Param("event") Event event);
}

