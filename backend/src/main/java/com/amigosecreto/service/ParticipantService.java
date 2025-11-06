package com.amigosecreto.service;

import com.amigosecreto.dto.request.ParticipantRequest;
import com.amigosecreto.dto.response.ParticipantResponse;
import com.amigosecreto.model.Event;
import com.amigosecreto.model.Participant;
import com.amigosecreto.model.User;
import com.amigosecreto.repository.EventRepository;
import com.amigosecreto.repository.ParticipantRepository;
import com.amigosecreto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para operações de participantes.
 */
@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Adiciona um participante a um evento.
     *
     * @param eventId ID do evento
     * @param request Dados do participante
     * @return ParticipantResponse
     * @throws RuntimeException se evento não encontrado, não pertence ao usuário ou email já existe
     */
    @Transactional
    public ParticipantResponse addParticipant(Long eventId, ParticipantRequest request) {
        User user = getCurrentUser();
        Event event = eventRepository.findByIdAndUser(eventId, user)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        // Verificar se evento já foi sorteado
        if (event.getStatus() == Event.EventStatus.DRAWN || 
            event.getStatus() == Event.EventStatus.COMPLETED) {
            throw new RuntimeException("Não é possível adicionar participantes a um evento já sorteado");
        }

        // Verificar se email já existe no evento
        if (participantRepository.existsByEmailAndEvent(request.getEmail(), event)) {
            throw new RuntimeException("Email já cadastrado neste evento");
        }

        Participant participant = new Participant();
        participant.setName(request.getName());
        participant.setEmail(request.getEmail());
        participant.setEvent(event);

        Participant savedParticipant = participantRepository.save(participant);
        return ParticipantResponse.fromEntity(savedParticipant);
    }

    /**
     * Lista todos os participantes de um evento.
     *
     * @param eventId ID do evento
     * @return Lista de ParticipantResponse
     * @throws RuntimeException se evento não encontrado ou não pertence ao usuário
     */
    @Transactional(readOnly = true)
    public List<ParticipantResponse> getParticipantsByEvent(Long eventId) {
        User user = getCurrentUser();
        Event event = eventRepository.findByIdAndUser(eventId, user)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        List<Participant> participants = participantRepository.findByEventOrderByNameAsc(event);
        return participants.stream()
                .map(ParticipantResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Busca um participante específico.
     *
     * @param eventId ID do evento
     * @param participantId ID do participante
     * @return ParticipantResponse
     * @throws RuntimeException se evento ou participante não encontrado
     */
    @Transactional(readOnly = true)
    public ParticipantResponse getParticipantById(Long eventId, Long participantId) {
        User user = getCurrentUser();
        Event event = eventRepository.findByIdAndUser(eventId, user)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        Participant participant = participantRepository.findByIdAndEvent(participantId, event)
                .orElseThrow(() -> new RuntimeException("Participante não encontrado"));

        return ParticipantResponse.fromEntity(participant);
    }

    /**
     * Remove um participante de um evento.
     *
     * @param eventId ID do evento
     * @param participantId ID do participante
     * @throws RuntimeException se evento ou participante não encontrado, ou evento já sorteado
     */
    @Transactional
    public void removeParticipant(Long eventId, Long participantId) {
        User user = getCurrentUser();
        Event event = eventRepository.findByIdAndUser(eventId, user)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        // Verificar se evento já foi sorteado
        if (event.getStatus() == Event.EventStatus.DRAWN || 
            event.getStatus() == Event.EventStatus.COMPLETED) {
            throw new RuntimeException("Não é possível remover participantes de um evento já sorteado");
        }

        Participant participant = participantRepository.findByIdAndEvent(participantId, event)
                .orElseThrow(() -> new RuntimeException("Participante não encontrado"));

        participantRepository.delete(participant);
    }

    /**
     * Conta o número de participantes de um evento.
     *
     * @param eventId ID do evento
     * @return Número de participantes
     */
    @Transactional(readOnly = true)
    public long countParticipants(Long eventId) {
        User user = getCurrentUser();
        Event event = eventRepository.findByIdAndUser(eventId, user)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        return participantRepository.countByEvent(event);
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

