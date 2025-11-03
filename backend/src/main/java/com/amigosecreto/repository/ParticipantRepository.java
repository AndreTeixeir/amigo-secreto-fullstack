package com.amigosecreto.repository;

import com.amigosecreto.model.Event;
import com.amigosecreto.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de banco de dados da entidade Participant.
 */
@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    /**
     * Busca todos os participantes de um evento.
     *
     * @param event Evento
     * @return Lista de participantes do evento
     */
    List<Participant> findByEvent(Event event);

    /**
     * Busca todos os participantes de um evento ordenados por nome.
     *
     * @param event Evento
     * @return Lista de participantes ordenados por nome
     */
    List<Participant> findByEventOrderByNameAsc(Event event);

    /**
     * Busca um participante específico de um evento.
     *
     * @param id ID do participante
     * @param event Evento
     * @return Optional contendo o participante se encontrado
     */
    Optional<Participant> findByIdAndEvent(Long id, Event event);

    /**
     * Busca participante por email em um evento específico.
     *
     * @param email Email do participante
     * @param event Evento
     * @return Optional contendo o participante se encontrado
     */
    Optional<Participant> findByEmailAndEvent(String email, Event event);

    /**
     * Verifica se existe um participante com o email no evento.
     *
     * @param email Email do participante
     * @param event Evento
     * @return true se existir, false caso contrário
     */
    boolean existsByEmailAndEvent(String email, Event event);

    /**
     * Conta quantos participantes um evento possui.
     *
     * @param event Evento
     * @return Número de participantes
     */
    long countByEvent(Event event);

    /**
     * Deleta todos os participantes de um evento.
     *
     * @param event Evento
     */
    void deleteByEvent(Event event);
}

