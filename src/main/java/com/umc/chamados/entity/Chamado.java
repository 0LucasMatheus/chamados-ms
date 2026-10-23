package com.umc.chamados.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "chamados")
public class Chamado {

    @Id
    private String id;
    private String titulo;
    private String descricao;
    private StatusChamado status;

    // quem abriu, vem do token
    private String solicitanteId;
    private String solicitanteNome;
    private LocalDateTime dataAbertura;

    // fica null ate o tecnico resolver
    private String solucao;
    private String tecnicoId;
    private String tecnicoNome;
    private LocalDateTime dataResolucao;
}
