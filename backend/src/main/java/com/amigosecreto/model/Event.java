package com.amigosecreto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa um evento de amigo secreto.
 * Contém informações sobre o evento e seus participantes.
 */
@Entity
@Table(name = "events")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do evento é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String name;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    @Column(length = 500)
    private String description;

    @NotNull(message = "Data do evento é obrigatória")
    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @NotNull(message = "Status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EventStatus status = EventStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Draw> draws = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Enum para status do evento.
     */
    public enum EventStatus {
        PENDING,    // Aguardando sorteio
        DRAWN,      // Sorteio realizado
        COMPLETED,  // Evento finalizado
        CANCELLED   // Evento cancelado
    }

    /**
     * Construtor para criação de novo evento.
     */
    public Event(String name, String description, LocalDate eventDate, User user) {
        this.name = name;
        this.description = description;
        this.eventDate = eventDate;
        this.user = user;
        this.status = EventStatus.PENDING;
    }

    /**
     * Adiciona um participante ao evento.
     */
    public void addParticipant(Participant participant) {
        participants.add(participant);
        participant.setEvent(this);
    }

    /**
     * Remove um participante do evento.
     */
    public void removeParticipant(Participant participant) {
        participants.remove(participant);
        participant.setEvent(null);
    }
}

