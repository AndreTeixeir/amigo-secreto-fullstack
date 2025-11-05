package com.amigosecreto.dto.response;

import com.amigosecreto.model.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para resposta de evento.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {

    private Long id;
    private String name;
    private String description;
    private LocalDate eventDate;
    private String status;
    private Long userId;
    private String username;
    private Integer participantCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Converte entidade Event para EventResponse.
     *
     * @param event Entidade Event
     * @return EventResponse
     */
    public static EventResponse fromEntity(Event event) {
        EventResponse response = new EventResponse();
        response.setId(event.getId());
        response.setName(event.getName());
        response.setDescription(event.getDescription());
        response.setEventDate(event.getEventDate());
        response.setStatus(event.getStatus().name());
        response.setUserId(event.getUser().getId());
        response.setUsername(event.getUser().getUsername());
        response.setParticipantCount(event.getParticipants() != null ? event.getParticipants().size() : 0);
        response.setCreatedAt(event.getCreatedAt());
        response.setUpdatedAt(event.getUpdatedAt());
        return response;
    }
}

