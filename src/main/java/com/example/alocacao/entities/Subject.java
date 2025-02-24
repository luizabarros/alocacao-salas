package com.example.alocacao.entities;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Subject {
	@Id
    @GeneratedValue
    @UuidGenerator
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    private String name;
    
    @Column(name = "code_class")
    private String codClass;  
    
    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = true)  	
    private Professor professor;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCodClass() {
		return codClass;
	}

	public void setCodClass(String codClass) {
		this.codClass = codClass;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}


   
}
