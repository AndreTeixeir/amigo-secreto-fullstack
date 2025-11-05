package com.amigosecreto.controller;

import com.amigosecreto.dto.request.EventRequest;
import com.amigosecreto.dto.response.EventResponse;
import com.amigosecreto.dto.response.MessageResponse;
import com.amigosecreto.model.Event;
import com.amigosecreto.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para endpoints de eventos.
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    /**
     * Cria um novo evento.
     *
     * @param request Dados do evento
     * @return EventResponse
     */
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventRequest request) {
        EventResponse response = eventService.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Lista todos os eventos do usuário autenticado.
     *
     * @return Lista de EventResponse
     */
    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<EventResponse> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    /**
     * Busca um evento específico.
     *
     * @param id ID do evento
     * @return EventResponse
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        EventResponse event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    /**
     * Atualiza um evento.
     *
     * @param id ID do evento
     * @param request Novos dados do evento
     * @return EventResponse
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventRequest request) {
        EventResponse response = eventService.updateEvent(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Deleta um evento.
     *
     * @param id ID do evento
     * @return MessageResponse
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok(new MessageResponse("Evento deletado com sucesso"));
    }

    /**
     * Lista eventos por status.
     *
     * @param status Status do evento
     * @return Lista de EventResponse
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<EventResponse>> getEventsByStatus(@PathVariable String status) {
        Event.EventStatus eventStatus = Event.EventStatus.valueOf(status.toUpperCase());
        List<EventResponse> events = eventService.getEventsByStatus(eventStatus);
        return ResponseEntity.ok(events);
    }

    /**
     * Atualiza o status de um evento.
     *
     * @param id ID do evento
     * @param status Novo status
     * @return EventResponse
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<EventResponse> updateEventStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        Event.EventStatus eventStatus = Event.EventStatus.valueOf(status.toUpperCase());
        EventResponse response = eventService.updateEventStatus(id, eventStatus);
        return ResponseEntity.ok(response);
    }
}

