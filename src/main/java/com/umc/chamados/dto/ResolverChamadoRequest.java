package com.umc.chamados.dto;

import jakarta.validation.constraints.NotBlank;

// o que chega pra resolver, so a solucao
// tecnico e data saem do token e do servidor
public record ResolverChamadoRequest(
        @NotBlank(message = "Informe a solução aplicada.") String solucao) {
}
