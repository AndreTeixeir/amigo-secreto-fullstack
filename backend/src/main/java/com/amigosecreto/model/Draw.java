package com.amigosecreto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entidade que representa o resultado de um sorteio de amigo secreto.
 * Armazena quem tirou quem no sorteio.
 */
@Entity
@Table(name = "draws")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Draw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @NotNull(message = "Giver é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giver_id", nullable = false)
    private Participant giver;

    @NotNull(message = "Receiver é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Participant receiver;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Construtor para criação de novo sorteio.
     */
    public Draw(Event event, Participant giver, Participant receiver) {
        this.event = event;
        this.giver = giver;
        this.receiver = receiver;
    }
}

