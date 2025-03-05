package com.example.alocacao.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.alocacao.dtos.LoginDTO;
import com.example.alocacao.dtos.ProfessorDTO;
import com.example.alocacao.dtos.ProfessorRegisterDTO;
import com.example.alocacao.dtos.TokenDTO;
import com.example.alocacao.services.ProfessorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/professor")
@Tag(name = "Professores", description = "Gerenciamento de professores e autenticação")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @PostMapping("/public/register")
    @Operation(summary = "Registrar um novo professor", description = "Cria um novo professor no sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Professor registrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro na requisição"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<ProfessorDTO> register(@RequestBody ProfessorRegisterDTO user) {
        ProfessorDTO professor = professorService.register(user);
        return ResponseEntity.ok(professor);
    }

    @PostMapping("/public/login")
    @Operation(summary = "Autenticar professor", description = "Faz login no sistema e retorna um token JWT.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginRequest) {
        return ResponseEntity.ok(professorService.login(loginRequest));
    }

    @GetMapping("/private/all")
    @Operation(summary = "Listar professor", description = "Lista professores.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso."),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<ProfessorDTO>> getAllProfessors() {
        List<ProfessorDTO> professors = professorService.getAllProfessors();
        return ResponseEntity.ok(professors);
    }

    @PutMapping("/private/all/{id}")
    @Operation(summary = "Atualizar um professor", description = "Atualiza as informações de um professor existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Professor atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Professor não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<ProfessorDTO> update(@PathVariable UUID id, @RequestBody ProfessorRegisterDTO professorRegisterDTO) {
        ProfessorDTO updatedProfessor = professorService.update(id, professorRegisterDTO);
        return ResponseEntity.ok(updatedProfessor);
    }

    @DeleteMapping("/private/all/{id}")
    @Operation(summary = "Excluir um professor", description = "Exclui um professor existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Professor excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Professor não encontrado"),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        professorService.delete(id);
        return ResponseEntity.ok().build();
    }
}