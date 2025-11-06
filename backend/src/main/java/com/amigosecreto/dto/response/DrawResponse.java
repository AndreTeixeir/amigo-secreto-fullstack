package com.amigosecreto.dto.response;

import com.amigosecreto.model.Draw;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para resposta de sorteio.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrawResponse {

    private Long id;
    private Long eventId;
    private String eventName;
    private Long giverId;
    private String giverName;
    private String giverEmail;
    private Long receiverId;
    private String receiverName;
    private String receiverEmail;
    private LocalDateTime createdAt;

    /**
     * Converte entidade Draw para DrawResponse.
     *
     * @param draw Entidade Draw
     * @return DrawResponse
     */
    public static DrawResponse fromEntity(Draw draw) {
        DrawResponse response = new DrawResponse();
        response.setId(draw.getId());
        response.setEventId(draw.getEvent().getId());
        response.setEventName(draw.getEvent().getName());
        response.setGiverId(draw.getGiver().getId());
        response.setGiverName(draw.getGiver().getName());
        response.setGiverEmail(draw.getGiver().getEmail());
        response.setReceiverId(draw.getReceiver().getId());
        response.setReceiverName(draw.getReceiver().getName());
        response.setReceiverEmail(draw.getReceiver().getEmail());
        response.setCreatedAt(draw.getCreatedAt());
        return response;
    }
}

