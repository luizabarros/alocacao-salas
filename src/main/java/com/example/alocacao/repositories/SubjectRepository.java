package com.example.alocacao.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.alocacao.entities.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, UUID> {

	boolean existsByNameAndCodClass(String name, String codClass);

}
