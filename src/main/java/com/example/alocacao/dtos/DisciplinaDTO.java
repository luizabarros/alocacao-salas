package com.example.alocacao.dtos;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public class DisciplinaDTO {

    @Schema(description = "Identificador único da disciplina", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Nome da disciplina", example = "Matemática")
    private String nome;

    @Schema(description = "Código da turma", example = "MAT101")
    private String codigoTurma;

    @Schema(description = "Professor associado à disciplina (opcional)")
    private ProfessorDTO professor; 
    
//    public DisciplinaDTO() {}
    	
    public DisciplinaDTO(UUID id, String nome, String codigoTurma, ProfessorDTO professor) {
        this.id = id;
        this.nome = nome;
        this.codigoTurma = codigoTurma;
        this.professor = professor;
    }

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCodigoTurma() {
		return codigoTurma;
	}

	public void setCodigoTurma(String codigoTurma) {
		this.codigoTurma = codigoTurma;
	}

	public ProfessorDTO getProfessor() {
		return professor;
	}

	public void setProfessor(ProfessorDTO professor) {
		this.professor = professor;
	}
    
    

}