package com.example.alocacao.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.alocacao.dtos.DisciplinaDTO;
import com.example.alocacao.dtos.ProfessorDTO;
import com.example.alocacao.entities.Disciplina;
import com.example.alocacao.entities.Professor;
import com.example.alocacao.repositories.DisciplinaRepository;
import com.example.alocacao.repositories.ProfessorRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Service
@Tag(name = "Disciplinas", description = "Serviço para gerenciamento de disciplinas") // Grupo no Swagger
public class DisciplinaService {

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Operation(summary = "Criar uma nova disciplina", description = "Cadastra uma disciplina identificada pelo nome e código da turma, com professor opcional.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Disciplina criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Disciplina já existente ou professor não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public Disciplina salvarDisciplina(DisciplinaDTO disciplinaDTO) {
        boolean existeDisciplina = disciplinaRepository.existsByNomeAndCodigoTurma(
                disciplinaDTO.getNome(), disciplinaDTO.getCodigoTurma());

        if (existeDisciplina) {
            throw new IllegalArgumentException("Já existe uma disciplina com esse nome e código de turma.");
        }

        Disciplina disciplina = new Disciplina();
        disciplina.setNome(disciplinaDTO.getNome());
        disciplina.setCodigoTurma(disciplinaDTO.getCodigoTurma());

        if (disciplinaDTO.getProfessor() != null) {
            Professor professor = professorRepository.findById(disciplinaDTO.getProfessor().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado!"));
            disciplina.setProfessor(professor);
        }

        return disciplinaRepository.save(disciplina);
    }

    @Operation(summary = "Atualizar professor de uma disciplina", description = "Permite associar ou modificar o professor de uma disciplina existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Professor atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Disciplina ou professor não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public Disciplina atualizarProfessor(UUID disciplinaId, UUID professorId) {
        Disciplina disciplina = disciplinaRepository.findById(disciplinaId)
                .orElseThrow(() -> new IllegalArgumentException("Disciplina não encontrada!"));

        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado!"));

        disciplina.setProfessor(professor);
        return disciplinaRepository.save(disciplina);
    }

    @Operation(summary = "Listar todas as disciplinas", description = "Retorna uma lista com todas as disciplinas cadastradas no sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de disciplinas retornada com sucesso")
    public List<DisciplinaDTO> listarDisciplinas() {
        return disciplinaRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Operation(summary = "Buscar uma disciplina por ID", description = "Retorna os detalhes de uma disciplina específica pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Disciplina encontrada"),
        @ApiResponse(responseCode = "404", description = "Disciplina não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public Optional<DisciplinaDTO> buscarPorId(UUID id) {
        return disciplinaRepository.findById(id).map(this::convertToDTO);
    }

    @Operation(summary = "Atualizar uma disciplina", description = "Atualiza os dados de uma disciplina existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Disciplina atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Disciplina não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public Optional<Disciplina> atualizarDisciplina(UUID id, DisciplinaDTO novaDisciplinaDTO) {
        return disciplinaRepository.findById(id).map(disciplina -> {
            disciplina.setNome(novaDisciplinaDTO.getNome());
            disciplina.setCodigoTurma(novaDisciplinaDTO.getCodigoTurma());

            if (novaDisciplinaDTO.getProfessor() != null) {
                Optional<Professor> professorOpt = professorRepository.findById(novaDisciplinaDTO.getProfessor().getId());
                professorOpt.ifPresent(disciplina::setProfessor);
            }

            return disciplinaRepository.save(disciplina);
        });
    }

    @Operation(summary = "Deletar uma disciplina", description = "Remove uma disciplina do sistema pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Disciplina deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Disciplina não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public void deletarDisciplina(UUID id) {
        disciplinaRepository.deleteById(id);
    }

    private DisciplinaDTO convertToDTO(Disciplina disciplina) {
        ProfessorDTO professorDTO = null;

        if (disciplina.getProfessor() != null) {
            professorDTO = new ProfessorDTO(
                disciplina.getProfessor().getId(),
                disciplina.getProfessor().getName(),
                disciplina.getProfessor().getEmail(),
                disciplina.getProfessor().isConfirmed()
            );
        }

        return new DisciplinaDTO(
        	disciplina.getId(),
            disciplina.getNome(),
            disciplina.getCodigoTurma(),
            professorDTO
        );
    }
}
