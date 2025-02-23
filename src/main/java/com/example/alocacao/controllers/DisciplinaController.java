package com.example.alocacao.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.alocacao.dtos.DisciplinaDTO;
import com.example.alocacao.entities.Disciplina;
import com.example.alocacao.services.DisciplinaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/disciplinas")
@Tag(name = "Disciplinas", description = "Gerenciamento das disciplinas") // Grupo no Swagger UI
public class DisciplinaController {

    @Autowired
    private DisciplinaService disciplinaService;

    @PostMapping
    @Operation(summary = "Criar uma nova disciplina", description = "Cadastra uma disciplina identificada pelo nome e código da turma, com professor opcional.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Disciplina criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Disciplina já existente"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<?> criarDisciplina(@RequestBody DisciplinaDTO disciplinaDTO) {
        try {
            Disciplina novaDisciplina = disciplinaService.salvarDisciplina(disciplinaDTO);
            return ResponseEntity.ok(novaDisciplina);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}/professor/{professorId}")
    @Operation(summary = "Atualizar ou adicionar um professor à disciplina", description = "Permite associar ou modificar um professor de uma disciplina.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Professor atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Disciplina ou professor não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<?> atualizarProfessor(@PathVariable UUID id, @PathVariable UUID professorId) {
        try {
            Disciplina disciplinaAtualizada = disciplinaService.atualizarProfessor(id, professorId);
            return ResponseEntity.ok(disciplinaAtualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Listar todas as disciplinas", description = "Retorna uma lista com todas as disciplinas cadastradas no sistema.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<DisciplinaDTO>> listarDisciplinas() {
        return ResponseEntity.ok(disciplinaService.listarDisciplinas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar uma disciplina por ID", description = "Retorna os detalhes de uma disciplina específica pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Disciplina encontrada"),
        @ApiResponse(responseCode = "404", description = "Disciplina não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Optional<DisciplinaDTO>> buscarDisciplina(@PathVariable UUID id) {
        return ResponseEntity.ok(disciplinaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma disciplina", description = "Atualiza os dados de uma disciplina existente, informando o ID e os novos valores.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Disciplina atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Disciplina não encontrada"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Optional<Disciplina>> atualizarDisciplina(@PathVariable UUID id, @RequestBody DisciplinaDTO disciplinaDTO) {
        return ResponseEntity.ok(disciplinaService.atualizarDisciplina(id, disciplinaDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar uma disciplina", description = "Remove uma disciplina do sistema pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Disciplina deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Disciplina não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> deletarDisciplina(@PathVariable UUID id) {
        disciplinaService.deletarDisciplina(id);
        return ResponseEntity.noContent().build();
    }
}
