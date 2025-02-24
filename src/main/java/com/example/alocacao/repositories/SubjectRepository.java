package com.example.alocacao.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.alocacao.entities.Disciplina;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, UUID> {

	boolean existsByNomeAndCodigoTurma(String nome, String codigoTurma);

}
