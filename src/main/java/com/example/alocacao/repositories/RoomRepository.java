package com.example.alocacao.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.alocacao.entities.Sala;

public interface SalaRepository extends JpaRepository<Sala, UUID> {

}
