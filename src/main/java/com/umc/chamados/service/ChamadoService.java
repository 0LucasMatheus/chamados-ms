package com.umc.chamados.service;

import com.umc.chamados.dto.AbrirChamadoRequest;
import com.umc.chamados.entity.Chamado;
import com.umc.chamados.entity.StatusChamado;
import com.umc.chamados.repository.ChamadoRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChamadoService {

    @Autowired
    private ChamadoRepository chamadoRepository;

    public Chamado abrir(AbrirChamadoRequest request, String solicitanteId, String solicitanteNome) {
        Chamado chamado = new Chamado();
        chamado.setTitulo(request.titulo());
        chamado.setDescricao(request.descricao());

        // preenchido aqui, n vem do json
        chamado.setStatus(StatusChamado.ABERTO);
        chamado.setSolicitanteId(solicitanteId);
        chamado.setSolicitanteNome(solicitanteNome);
        chamado.setDataAbertura(LocalDateTime.now());

        return chamadoRepository.save(chamado);
    }

    public List<Chamado> listarDoUsuario(String solicitanteId) {
        return chamadoRepository.findBySolicitanteId(solicitanteId);
    }
}
