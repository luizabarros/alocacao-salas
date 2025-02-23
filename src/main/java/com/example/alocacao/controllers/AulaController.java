package com.example.alocacao.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.alocacao.dtos.AulaDTO;
import com.example.alocacao.entities.Aula;
import com.example.alocacao.services.AulaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/aulas")
@Tag(name = "Aulas", description = "Gerenciamento das alocações de aulas") // Grupo no Swagger UI
public class AulaController {

    @Autowired
    private AulaService aulaService;

    @PostMapping
    @Operation(summary = "Criar uma nova aula", description = "Aloca uma aula para uma sala e horário específicos, respeitando o intervalo de 50 minutos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aula criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Horário indisponível"),
        @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<?> criarAula(@RequestBody AulaDTO aulaDTO) {
        try {
            Aula novaAula = aulaService.salvarAula(aulaDTO);
            return ResponseEntity.ok(novaAula);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Listar todas as aulas", description = "Retorna uma lista com todas as aulas cadastradas.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<AulaDTO>> listarAulas() {
        return ResponseEntity.ok(aulaService.listarAulas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar uma aula por ID", description = "Retorna os detalhes de uma aula específica pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aula encontrada"),
        @ApiResponse(responseCode = "404", description = "Aula não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Optional<AulaDTO>> buscarAula(@PathVariable UUID id) {
        return ResponseEntity.ok(aulaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma aula", description = "Atualiza os dados de uma aula existente, respeitando a regra dos 50 minutos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aula atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Horário indisponível ou requisição inválida"),
        @ApiResponse(responseCode = "404", description = "Aula não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<?> atualizarAula(@PathVariable UUID id, @RequestBody AulaDTO aulaDTO) {
        try {
            Optional<Aula> aulaAtualizada = aulaService.atualizarAula(id, aulaDTO);
            if (aulaAtualizada.isPresent()) {
                return ResponseEntity.ok(aulaAtualizada.get()); // Retorna a aula atualizada
            } else {
                return ResponseEntity.status(404).body("Aula não encontrada");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar uma aula", description = "Remove uma aula do sistema pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Aula deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Aula não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<?> deletarAula(@PathVariable UUID id) {
        try {
            aulaService.deletarAula(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
