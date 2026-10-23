package com.umc.chamados.controller;

import com.umc.chamados.dto.AbrirChamadoRequest;
import com.umc.chamados.entity.Chamado;
import com.umc.chamados.service.ChamadoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chamados")
public class ChamadoController {

    @Autowired
    private ChamadoService chamadoService;

    // solicitante sai do token
    @PreAuthorize("hasAuthority('chamados:abrir')")
    @PostMapping
    public ResponseEntity<Chamado> abrir(@Valid @RequestBody AbrirChamadoRequest request, @AuthenticationPrincipal Jwt token) {
        Chamado criado = chamadoService.abrir(request, token.getSubject(), token.getClaimAsString("nome"));
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    // o proprio usuario ve os dele, o tecnico ve de qualquer um
    @PreAuthorize("#id == authentication.name or hasAuthority('chamados:resolver')")
    @GetMapping("/usuario/{id}")
    public List<Chamado> listarDoUsuario(@PathVariable String id) {
        return chamadoService.listarDoUsuario(id);
    }

    // so da pra saber o dono depois de buscar, entao confere na volta
    @PostAuthorize("returnObject.solicitanteId == authentication.name or hasAuthority('chamados:resolver')")
    @GetMapping("/{id}")
    public Chamado buscar(@PathVariable String id) {
        return chamadoService.buscar(id);
    }
}
