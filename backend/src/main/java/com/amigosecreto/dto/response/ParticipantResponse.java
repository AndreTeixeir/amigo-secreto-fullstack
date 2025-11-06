package com.amigosecreto.dto.response;

import com.amigosecreto.model.Participant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para resposta de participante.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantResponse {

    private Long id;
    private String name;
    private String email;
    private Long eventId;
    private String eventName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Converte entidade Participant para ParticipantResponse.
     *
     * @param participant Entidade Participant
     * @return ParticipantResponse
     */
    public static ParticipantResponse fromEntity(Participant participant) {
        ParticipantResponse response = new ParticipantResponse();
        response.setId(participant.getId());
        response.setName(participant.getName());
        response.setEmail(participant.getEmail());
        response.setEventId(participant.getEvent().getId());
        response.setEventName(participant.getEvent().getName());
        response.setCreatedAt(participant.getCreatedAt());
        response.setUpdatedAt(participant.getUpdatedAt());
        return response;
    }
}

