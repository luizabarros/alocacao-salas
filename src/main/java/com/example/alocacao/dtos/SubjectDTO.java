package com.example.alocacao.dtos;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public class SubjectDTO {

    @Schema(description = "Identificador único da disciplina", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Nome da disciplina", example = "Matemática")
    private String name; 

    @Schema(description = "Código da turma", example = "MAT101")
    private String codClass; 

    @Schema(description = "Identificador do professor associado à disciplina (opcional)", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID professorId;
    
    public SubjectDTO(UUID id, String name, String codClass, UUID professorId) {
        this.id = id;
        this.name = name;
        this.codClass = codClass;
        this.professorId = professorId;
    }

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

    public UUID getProfessorId() {
        return professorId;
    }

    public void setProfessorId(UUID professorId) {
        this.professorId = professorId;
    }
}
