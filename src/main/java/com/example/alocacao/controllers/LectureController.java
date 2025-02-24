package com.example.alocacao.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import com.example.alocacao.dtos.LectureDTO;
import com.example.alocacao.entities.Lecture;
import com.example.alocacao.services.LectureService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/lectures")
@Tag(name = "Aulas", description = "Gerenciamento das alocações de aulas") 
public class LectureController {

    @Autowired
    private LectureService lectureService;

    @Secured("ROLE_PROFESSOR") 
    @PostMapping
    @Operation(summary = "Criar uma nova aula", description = "Apenas professores podem alocar salas e horários para aulas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aula criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Horário indisponível ou inválido"),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<?> saveLecture(@RequestBody LectureDTO lectureDTO) {
        try {
            Lecture newLecture = lectureService.saveLecture(lectureDTO);
            return ResponseEntity.ok(newLecture);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Listar todas as aulas", description = "Retorna uma lista com todas as aulas cadastradas.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<LectureDTO>> listLectures() {
        return ResponseEntity.ok(lectureService.listLectures());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar uma aula por ID", description = "Retorna os detalhes de uma aula específica pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aula encontrada"),
        @ApiResponse(responseCode = "404", description = "Aula não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Optional<LectureDTO>> getByLectureId(@PathVariable UUID id) {
        return ResponseEntity.ok(lectureService.getByLectureId(id));
    }

    @Secured("ROLE_PROFESSOR") 
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma aula", description = "Atualiza os dados de uma aula existente, respeitando a regra dos 50 minutos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aula atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Horário indisponível ou requisição inválida"),
        @ApiResponse(responseCode = "404", description = "Aula não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<?> updateLecture(@PathVariable UUID id, @RequestBody LectureDTO lectureDTO) {
        try {
            Optional<Lecture> updatedLecture = lectureService.updateLecture(id, lectureDTO);
            if (updatedLecture.isPresent()) {
                return ResponseEntity.ok(updatedLecture.get());
            } else {
                return ResponseEntity.status(404).body("Aula não encontrada");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Secured("ROLE_PROFESSOR") 
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar uma aula", description = "Remove uma aula do sistema pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Aula deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Aula não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<?> deleteLecture(@PathVariable UUID id) {
        try {
            lectureService.deleteLecture(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
