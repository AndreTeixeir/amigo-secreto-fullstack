package com.amigosecreto.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para requisição de criação/atualização de evento.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {

    @NotBlank(message = "Nome do evento é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String name;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String description;

    @NotNull(message = "Data do evento é obrigatória")
    private LocalDate eventDate;
}

