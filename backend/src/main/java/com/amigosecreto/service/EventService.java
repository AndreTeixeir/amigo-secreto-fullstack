package com.amigosecreto.service;

import com.amigosecreto.dto.request.EventRequest;
import com.amigosecreto.dto.response.EventResponse;
import com.amigosecreto.model.Event;
import com.amigosecreto.model.User;
import com.amigosecreto.repository.EventRepository;
import com.amigosecreto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para operações de eventos.
 */
@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Cria um novo evento para o usuário autenticado.
     *
     * @param request Dados do evento
     * @return EventResponse
     */
    @Transactional
    public EventResponse createEvent(EventRequest request) {
        User user = getCurrentUser();

        Event event = new Event();
        event.setName(request.getName());
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());
        event.setStatus(Event.EventStatus.PENDING);
        event.setUser(user);

        Event savedEvent = eventRepository.save(event);
        return EventResponse.fromEntity(savedEvent);
    }

    /**
     * Lista todos os eventos do usuário autenticado.
     *
     * @return Lista de EventResponse
     */
    @Transactional(readOnly = true)
    public List<EventResponse> getAllEvents() {
        User user = getCurrentUser();
        List<Event> events = eventRepository.findByUserOrderByEventDateDesc(user);
        return events.stream()
                .map(EventResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Busca um evento específico do usuário autenticado.
     *
     * @param eventId ID do evento
     * @return EventResponse
     * @throws RuntimeException se evento não encontrado ou não pertence ao usuário
     */
    @Transactional(readOnly = true)
    public EventResponse getEventById(Long eventId) {
        User user = getCurrentUser();
        Event event = eventRepository.findByIdAndUser(eventId, user)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
        return EventResponse.fromEntity(event);
    }

    /**
     * Atualiza um evento do usuário autenticado.
     *
     * @param eventId ID do evento
     * @param request Novos dados do evento
     * @return EventResponse
     * @throws RuntimeException se evento não encontrado ou não pertence ao usuário
     */
    @Transactional
    public EventResponse updateEvent(Long eventId, EventRequest request) {
        User user = getCurrentUser();
        Event event = eventRepository.findByIdAndUser(eventId, user)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        event.setName(request.getName());
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());

        Event updatedEvent = eventRepository.save(event);
        return EventResponse.fromEntity(updatedEvent);
    }

    /**
     * Deleta um evento do usuário autenticado.
     *
     * @param eventId ID do evento
     * @throws RuntimeException se evento não encontrado ou não pertence ao usuário
     */
    @Transactional
    public void deleteEvent(Long eventId) {
        User user = getCurrentUser();
        Event event = eventRepository.findByIdAndUser(eventId, user)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        eventRepository.delete(event);
    }

    /**
     * Lista eventos do usuário autenticado por status.
     *
     * @param status Status do evento
     * @return Lista de EventResponse
     */
    @Transactional(readOnly = true)
    public List<EventResponse> getEventsByStatus(Event.EventStatus status) {
        User user = getCurrentUser();
        List<Event> events = eventRepository.findByUserAndStatus(user, status);
        return events.stream()
                .map(EventResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Atualiza o status de um evento.
     *
     * @param eventId ID do evento
     * @param status Novo status
     * @return EventResponse
     * @throws RuntimeException se evento não encontrado ou não pertence ao usuário
     */
    @Transactional
    public EventResponse updateEventStatus(Long eventId, Event.EventStatus status) {
        User user = getCurrentUser();
        Event event = eventRepository.findByIdAndUser(eventId, user)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        event.setStatus(status);
        Event updatedEvent = eventRepository.save(event);
        return EventResponse.fromEntity(updatedEvent);
    }

    /**
     * Obtém o usuário autenticado atualmente.
     *
     * @return User
     * @throws UsernameNotFoundException se usuário não encontrado
     */
    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }
}

