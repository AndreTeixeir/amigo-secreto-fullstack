package com.amigosecreto.repository;

import com.amigosecreto.model.Event;
import com.amigosecreto.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de banco de dados da entidade Event.
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Busca todos os eventos de um usuário.
     *
     * @param user Usuário proprietário dos eventos
     * @return Lista de eventos do usuário
     */
    List<Event> findByUser(User user);

    /**
     * Busca todos os eventos de um usuário ordenados por data.
     *
     * @param user Usuário proprietário dos eventos
     * @return Lista de eventos ordenados por data
     */
    List<Event> findByUserOrderByEventDateDesc(User user);

    /**
     * Busca eventos de um usuário por status.
     *
     * @param user Usuário proprietário dos eventos
     * @param status Status do evento
     * @return Lista de eventos com o status especificado
     */
    List<Event> findByUserAndStatus(User user, Event.EventStatus status);

    /**
     * Busca um evento específico de um usuário.
     *
     * @param id ID do evento
     * @param user Usuário proprietário do evento
     * @return Optional contendo o evento se encontrado
     */
    Optional<Event> findByIdAndUser(Long id, User user);

    /**
     * Busca eventos por data.
     *
     * @param eventDate Data do evento
     * @return Lista de eventos na data especificada
     */
    List<Event> findByEventDate(LocalDate eventDate);

    /**
     * Busca eventos entre duas datas.
     *
     * @param startDate Data inicial
     * @param endDate Data final
     * @return Lista de eventos no período
     */
    List<Event> findByEventDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Conta quantos eventos um usuário possui.
     *
     * @param user Usuário
     * @return Número de eventos
     */
    long countByUser(User user);

    /**
     * Busca eventos com participantes carregados.
     *
     * @param id ID do evento
     * @return Optional contendo o evento com participantes
     */
    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.participants WHERE e.id = :id")
    Optional<Event> findByIdWithParticipants(@Param("id") Long id);
}

