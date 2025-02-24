package com.example.alocacao.repositories;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.alocacao.entities.Aula;

@Repository
public interface AulaRepository extends JpaRepository<Aula, UUID> {

	boolean existsBySalaIdAndHoraInicio(UUID salaId, LocalDateTime horaInicio);

	boolean existsBySalaIdAndHoraInicioBetween(UUID salaId, LocalDateTime minusMinutes, LocalDateTime horaFimNovaAula);
}