package com.amigosecreto.service;

import com.amigosecreto.dto.response.DrawResponse;
import com.amigosecreto.model.Draw;
import com.amigosecreto.model.Event;
import com.amigosecreto.model.Participant;
import com.amigosecreto.model.User;
import com.amigosecreto.repository.DrawRepository;
import com.amigosecreto.repository.EventRepository;
import com.amigosecreto.repository.ParticipantRepository;
import com.amigosecreto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para operações de sorteio.
 */
@Service
public class DrawService {

    @Autowired
    private DrawRepository drawRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private UserRepository userRepository;

    private static final int MIN_PARTICIPANTS = 3;

    /**
     * Realiza o sorteio de amigo secreto para um evento.
     *
     * @param eventId ID do evento
     * @return Lista de DrawResponse
     * @throws RuntimeException se evento não encontrado, já sorteado ou com poucos participantes
     */
    @Transactional
    public List<DrawResponse> performDraw(Long eventId) {
        User user = getCurrentUser();
        Event event = eventRepository.findByIdAndUser(eventId, user)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        // Verificar se evento já foi sorteado
        if (drawRepository.existsByEvent(event)) {
            throw new RuntimeException("Este evento já foi sorteado");
        }

        // Buscar participantes
        List<Participant> participants = participantRepository.findByEvent(event);

        // Validar número mínimo de participantes
        if (participants.size() < MIN_PARTICIPANTS) {
            throw new RuntimeException("É necessário no mínimo " + MIN_PARTICIPANTS + " participantes para realizar o sorteio");
        }

        // Realizar sorteio
        List<Draw> draws = executeDrawAlgorithm(event, participants);

        // Salvar sorteios
        List<Draw> savedDraws = drawRepository.saveAll(draws);

        // Atualizar status do evento
        event.setStatus(Event.EventStatus.DRAWN);
        eventRepository.save(event);

        // Converter para DTO
        return savedDraws.stream()
                .map(DrawResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Algoritmo de sorteio de amigo secreto.
     * Garante que ninguém tira a si mesmo.
     *
     * @param event Evento
     * @param participants Lista de participantes
     * @return Lista de sorteios
     */
    private List<Draw> executeDrawAlgorithm(Event event, List<Participant> participants) {
        List<Participant> givers = new ArrayList<>(participants);
        List<Participant> receivers = new ArrayList<>(participants);
        
        boolean validDraw = false;
        List<Draw> draws = new ArrayList<>();
        int maxAttempts = 100;
        int attempts = 0;

        while (!validDraw && attempts < maxAttempts) {
            attempts++;
            draws.clear();
            Collections.shuffle(receivers);
            
            validDraw = true;
            for (int i = 0; i < givers.size(); i++) {
                if (givers.get(i).getId().equals(receivers.get(i).getId())) {
                    validDraw = false;
                    break;
                }
            }
        }

        if (!validDraw) {
            throw new RuntimeException("Não foi possível realizar o sorteio. Tente novamente.");
        }

        // Criar os sorteios
        for (int i = 0; i < givers.size(); i++) {
            Draw draw = new Draw();
            draw.setEvent(event);
            draw.setGiver(givers.get(i));
            draw.setReceiver(receivers.get(i));
            draws.add(draw);
        }

        return draws;
    }

    /**
     * Lista todos os sorteios de um evento.
     *
     * @param eventId ID do evento
     * @return Lista de DrawResponse
     * @throws RuntimeException se evento não encontrado ou não pertence ao usuário
     */
    @Transactional(readOnly = true)
    public List<DrawResponse> getDrawsByEvent(Long eventId) {
        User user = getCurrentUser();
        Event event = eventRepository.findByIdAndUser(eventId, user)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        List<Draw> draws = drawRepository.findByEventWithParticipants(event);
        return draws.stream()
                .map(DrawResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Busca o resultado do sorteio para um participante específico.
     * Retorna apenas quem o participante tirou (não revela quem o tirou).
     *
     * @param eventId ID do evento
     * @param participantId ID do participante
     * @return DrawResponse
     * @throws RuntimeException se evento, participante ou sorteio não encontrado
     */
    @Transactional(readOnly = true)
    public DrawResponse getDrawForParticipant(Long eventId, Long participantId) {
        User user = getCurrentUser();
        Event event = eventRepository.findByIdAndUser(eventId, user)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        Participant participant = participantRepository.findByIdAndEvent(participantId, event)
                .orElseThrow(() -> new RuntimeException("Participante não encontrado"));

        Draw draw = drawRepository.findByEventAndGiver(event, participant)
                .orElseThrow(() -> new RuntimeException("Sorteio não encontrado para este participante"));

        return DrawResponse.fromEntity(draw);
    }

    /**
     * Deleta todos os sorteios de um evento (permite refazer o sorteio).
     *
     * @param eventId ID do evento
     * @throws RuntimeException se evento não encontrado ou não pertence ao usuário
     */
    @Transactional
    public void deleteDraws(Long eventId) {
        User user = getCurrentUser();
        Event event = eventRepository.findByIdAndUser(eventId, user)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        drawRepository.deleteByEvent(event);

        // Voltar status do evento para PENDING
        event.setStatus(Event.EventStatus.PENDING);
        eventRepository.save(event);
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

