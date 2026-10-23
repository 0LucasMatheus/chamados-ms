package com.umc.chamados.repository;

import com.umc.chamados.entity.Chamado;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChamadoRepository extends MongoRepository<Chamado, String> {
}
