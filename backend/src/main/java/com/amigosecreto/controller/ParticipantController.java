package com.amigosecreto.controller;

import com.amigosecreto.dto.request.ParticipantRequest;
import com.amigosecreto.dto.response.MessageResponse;
import com.amigosecreto.dto.response.ParticipantResponse;
import com.amigosecreto.service.ParticipantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller para endpoints de participantes.
 */
@RestController
@RequestMapping("/api/events/{eventId}/participants")
public class ParticipantController {

    @Autowired
    private ParticipantService participantService;

    /**
     * Adiciona um participante a um evento.
     *
     * @param eventId ID do evento
     * @param request Dados do participante
     * @return ParticipantResponse
     */
    @PostMapping
    public ResponseEntity<ParticipantResponse> addParticipant(
            @PathVariable Long eventId,
            @Valid @RequestBody ParticipantRequest request) {
        ParticipantResponse response = participantService.addParticipant(eventId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Lista todos os participantes de um evento.
     *
     * @param eventId ID do evento
     * @return Lista de ParticipantResponse
     */
    @GetMapping
    public ResponseEntity<List<ParticipantResponse>> getParticipants(@PathVariable Long eventId) {
        List<ParticipantResponse> participants = participantService.getParticipantsByEvent(eventId);
        return ResponseEntity.ok(participants);
    }

    /**
     * Busca um participante específico.
     *
     * @param eventId ID do evento
     * @param participantId ID do participante
     * @return ParticipantResponse
     */
    @GetMapping("/{participantId}")
    public ResponseEntity<ParticipantResponse> getParticipant(
            @PathVariable Long eventId,
            @PathVariable Long participantId) {
        ParticipantResponse participant = participantService.getParticipantById(eventId, participantId);
        return ResponseEntity.ok(participant);
    }

    /**
     * Remove um participante de um evento.
     *
     * @param eventId ID do evento
     * @param participantId ID do participante
     * @return MessageResponse
     */
    @DeleteMapping("/{participantId}")
    public ResponseEntity<MessageResponse> removeParticipant(
            @PathVariable Long eventId,
            @PathVariable Long participantId) {
        participantService.removeParticipant(eventId, participantId);
        return ResponseEntity.ok(new MessageResponse("Participante removido com sucesso"));
    }

    /**
     * Conta o número de participantes de um evento.
     *
     * @param eventId ID do evento
     * @return Mapa com a contagem
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countParticipants(@PathVariable Long eventId) {
        long count = participantService.countParticipants(eventId);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
}

