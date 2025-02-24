package com.example.alocacao.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import com.example.alocacao.dtos.SalaDTO;
import com.example.alocacao.entities.Sala;
import com.example.alocacao.services.SalaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/salas")
@Secured("ROLE_PROFESSOR")
@Tag(name = "Salas", description = "Gerenciamento das salas disponíveis") // Grupo no Swagger UI
public class SalaController {

    @Autowired
    private SalaService salaService;

    @PostMapping
    @Operation(summary = "Criar uma nova sala", description = "Cria uma nova sala informando o nome.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sala criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Sala> criarSala(@RequestBody SalaDTO salaDTO) {
        Sala novaSala = salaService.salvarSala(salaDTO);
        return ResponseEntity.ok(novaSala);
    }

    @GetMapping
    @Operation(summary = "Listar todas as salas", description = "Retorna uma lista com todas as salas cadastradas no sistema.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<SalaDTO>> listarSalas() {
        return ResponseEntity.ok(salaService.listarSalas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar uma sala por ID", description = "Retorna os detalhes de uma sala específica pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sala encontrada"),
        @ApiResponse(responseCode = "404", description = "Sala não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Optional<SalaDTO>> buscarSala(@PathVariable UUID id) {
        return ResponseEntity.ok(salaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma sala", description = "Atualiza os dados de uma sala existente, informando o ID e os novos valores.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sala atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Sala não encontrada"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Optional<Sala>> atualizarSala(@PathVariable UUID id, @RequestBody SalaDTO salaDTO) {
        return ResponseEntity.ok(salaService.atualizarSala(id, salaDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar uma sala", description = "Remove uma sala do sistema pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Sala deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Sala não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> deletarSala(@PathVariable UUID id) {
        salaService.deletarSala(id);
        return ResponseEntity.noContent().build();
    }
}
