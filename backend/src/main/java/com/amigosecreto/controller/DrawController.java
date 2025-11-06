package com.amigosecreto.controller;

import com.amigosecreto.dto.response.DrawResponse;
import com.amigosecreto.dto.response.MessageResponse;
import com.amigosecreto.service.DrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para endpoints de sorteio.
 */
@RestController
@RequestMapping("/api/events/{eventId}/draw")
public class DrawController {

    @Autowired
    private DrawService drawService;

    /**
     * Realiza o sorteio de amigo secreto para um evento.
     *
     * @param eventId ID do evento
     * @return Lista de DrawResponse
     */
    @PostMapping
    public ResponseEntity<List<DrawResponse>> performDraw(@PathVariable Long eventId) {
        List<DrawResponse> draws = drawService.performDraw(eventId);
        return ResponseEntity.ok(draws);
    }

    /**
     * Lista todos os sorteios de um evento.
     *
     * @param eventId ID do evento
     * @return Lista de DrawResponse
     */
    @GetMapping
    public ResponseEntity<List<DrawResponse>> getDraws(@PathVariable Long eventId) {
        List<DrawResponse> draws = drawService.getDrawsByEvent(eventId);
        return ResponseEntity.ok(draws);
    }

    /**
     * Busca o resultado do sorteio para um participante específico.
     *
     * @param eventId ID do evento
     * @param participantId ID do participante
     * @return DrawResponse
     */
    @GetMapping("/participant/{participantId}")
    public ResponseEntity<DrawResponse> getDrawForParticipant(
            @PathVariable Long eventId,
            @PathVariable Long participantId) {
        DrawResponse draw = drawService.getDrawForParticipant(eventId, participantId);
        return ResponseEntity.ok(draw);
    }

    /**
     * Deleta todos os sorteios de um evento (permite refazer o sorteio).
     *
     * @param eventId ID do evento
     * @return MessageResponse
     */
    @DeleteMapping
    public ResponseEntity<MessageResponse> deleteDraws(@PathVariable Long eventId) {
        drawService.deleteDraws(eventId);
        return ResponseEntity.ok(new MessageResponse("Sorteios deletados com sucesso. Evento voltou ao status PENDING."));
    }
}

