package com.umc.chamados.dto;

import jakarta.validation.constraints.NotBlank;

// o que chega pra abrir um chamado
public record AbrirChamadoRequest(
        @NotBlank(message = "Informe o título.") String titulo,
        @NotBlank(message = "Informe a descrição.") String descricao) {
}
