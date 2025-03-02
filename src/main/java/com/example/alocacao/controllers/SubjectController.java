package com.example.alocacao.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import com.example.alocacao.dtos.SubjectDTO;
import com.example.alocacao.entities.Subject;
import com.example.alocacao.services.SubjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/disciplinas")
@Tag(name = "Disciplinas", description = "Gerenciamento das disciplinas")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @PostMapping
    @Secured("ROLE_PROFESSOR")
    @Operation(summary = "Criar uma nova disciplina", description = "Cadastra uma disciplina identificada pelo nome e código da turma, com professor opcional.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Disciplina criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Disciplina já existente ou professor não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<?> saveSubject(@RequestBody SubjectDTO subjectDTO) {
        try {
            Subject newSubject = subjectService.saveSubject(subjectDTO);
            return ResponseEntity.status(201).body(newSubject);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/professor/{professorId}")
    @Secured("ROLE_PROFESSOR")
    @Operation(summary = "Atualizar ou adicionar um professor à disciplina", description = "Permite associar ou modificar um professor de uma disciplina.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Professor atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Disciplina ou professor não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<?> updateSubjectProfessor(@PathVariable UUID id, @PathVariable UUID professorId) {
        try {
            Subject subjectProfessorUpdated = subjectService.updateSubjectProfessor(id, professorId);
            return ResponseEntity.ok(subjectProfessorUpdated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Listar todas as disciplinas", description = "Retorna uma lista com todas as disciplinas cadastradas no sistema.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<SubjectDTO>> listSubjects() {
        List<SubjectDTO> subjects = subjectService.listSubjects();
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar uma disciplina por ID", description = "Retorna os detalhes de uma disciplina específica pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Disciplina encontrada"),
        @ApiResponse(responseCode = "404", description = "Disciplina não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<?> getSubjectById(@PathVariable UUID id) {
        Optional<SubjectDTO> disciplinaOpt = subjectService.getSubjectById(id);
        if (disciplinaOpt.isPresent()) {
            return ResponseEntity.ok(disciplinaOpt.get());
        } else {
            return ResponseEntity.status(404).body("Disciplina não encontrada");
        }
    }

    @PutMapping("/{id}")
    @Secured("ROLE_PROFESSOR")
    @Operation(summary = "Atualizar uma disciplina", description = "Atualiza os dados de uma disciplina existente, informando o ID e os novos valores.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Disciplina atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Disciplina não encontrada"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<?> updateSubject(@PathVariable UUID id, @RequestBody SubjectDTO subjectDTO) {
        Optional<Subject> updatedSubject = subjectService.updateSubject(id, subjectDTO);
        if (updatedSubject.isPresent()) {
            return ResponseEntity.ok(updatedSubject.get());
        } else {
            return ResponseEntity.status(404).body("Disciplina não encontrada");
        }
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_PROFESSOR")
    @Operation(summary = "Deletar uma disciplina", description = "Remove uma disciplina do sistema pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Disciplina deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Disciplina não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<?> deleteSubject(@PathVariable UUID id) {
        boolean deleted = subjectService.deleteSubject(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(404).body("Disciplina não encontrada");
        }
    }
}
