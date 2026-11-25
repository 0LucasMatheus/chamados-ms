package com.umc.chamados.service;

import com.umc.chamados.dto.AbrirChamadoRequest;
import com.umc.chamados.dto.ResolverChamadoRequest;
import com.umc.chamados.entity.Chamado;
import com.umc.chamados.entity.StatusChamado;
import com.umc.chamados.repository.ChamadoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
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

    public List<Chamado> listarTodos() {
        return chamadoRepository.findAll();
    }

    public List<Chamado> listarDoUsuario(String solicitanteId) {
        return chamadoRepository.findBySolicitanteId(solicitanteId);
    }

    public Chamado buscar(String id) {
        return chamadoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Chamado não encontrado."));
    }

    public Chamado resolver(String id, ResolverChamadoRequest request, String tecnicoId, String tecnicoNome) {
        Chamado chamado = buscar(id);

        // resolvido n pode ser alterado de novo
        if (chamado.getStatus() == StatusChamado.RESOLVIDO) {
            throw new IllegalStateException("Esse chamado já foi resolvido.");
        }

        chamado.setSolucao(request.solucao());
        chamado.setTecnicoId(tecnicoId);
        chamado.setTecnicoNome(tecnicoNome);
        chamado.setDataResolucao(LocalDateTime.now());
        chamado.setStatus(StatusChamado.RESOLVIDO);

        return chamadoRepository.save(chamado);
    }
}
