package com.example.alocacao.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.alocacao.dtos.SubjectDTO;
import com.example.alocacao.dtos.ProfessorDTO;
import com.example.alocacao.dtos.RoleDTO;
import com.example.alocacao.entities.Subject;
import com.example.alocacao.entities.Professor;
import com.example.alocacao.repositories.SubjectRepository;
import com.example.alocacao.repositories.ProfessorRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Service
@Tag(name = "Disciplinas", description = "Serviço para gerenciamento de disciplinas")
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Operation(summary = "Criar uma nova disciplina", description = "Cadastra uma disciplina identificada pelo nome e código da turma, com professor opcional.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Disciplina criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Disciplina já existente ou professor não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public Subject saveSubject(SubjectDTO subjectDTO) {
        boolean existsSubject = subjectRepository.existsByNameAndCodClass(
                subjectDTO.getName(), subjectDTO.getCodClass());

        if (existsSubject) {
            throw new IllegalArgumentException("Já existe uma disciplina com esse nome e código de turma.");
        }

        Subject subject = new Subject();
        subject.setName(subjectDTO.getName());
        subject.setCodClass(subjectDTO.getCodClass());

        if (subjectDTO.getProfessorId() != null) {
            Professor professor = professorRepository.findById(subjectDTO.getProfessorId())
                .orElse(null);

            if (professor != null) {
            	subject.setProfessor(professor);
            } else {
                System.out.println("⚠️ Professor não encontrado, a disciplina será salva sem professor.");
            }
        }


        return subjectRepository.save(subject);
    }


    @Operation(summary = "Atualizar professor de uma disciplina", description = "Permite associar ou modificar o professor de uma disciplina existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Professor atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Disciplina ou professor não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public Subject updateSubjectProfessor(UUID subjectId, UUID professorId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Disciplina não encontrada!"));

        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado!"));

        subject.setProfessor(professor);
        return subjectRepository.save(subject);
    }

    @Operation(summary = "Listar todas as disciplinas", description = "Retorna uma lista com todas as disciplinas cadastradas no sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de disciplinas retornada com sucesso")
    public List<SubjectDTO> listSubjects() {
        return subjectRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Operation(summary = "Buscar uma disciplina por ID", description = "Retorna os detalhes de uma disciplina específica pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Disciplina encontrada"),
        @ApiResponse(responseCode = "404", description = "Disciplina não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public Optional<SubjectDTO> getSubjectById(UUID id) {
        return subjectRepository.findById(id).map(this::convertToDTO);
    }

    @Operation(summary = "Atualizar uma disciplina", description = "Atualiza os dados de uma disciplina existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Disciplina atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Disciplina não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public Optional<Subject> updateSubject(UUID id, SubjectDTO newSubjectDTO) {
        Optional<Subject> optionalDisc = subjectRepository.findById(id);

        if (optionalDisc.isEmpty()) {
            return Optional.empty();
        }

        Subject subject = optionalDisc.get();
        subject.setName(newSubjectDTO.getName());
        subject.setCodClass(newSubjectDTO.getCodClass());

        if (newSubjectDTO.getProfessorId() != null) {
            Professor professor = professorRepository.findById(newSubjectDTO.getProfessorId())
                .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado!"));
            subject.setProfessor(professor);
        }

        return Optional.of(subjectRepository.save(subject));
    }


    @Operation(summary = "Deletar uma disciplina", description = "Remove uma disciplina do sistema pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Disciplina deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Disciplina não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public boolean deleteSubject(UUID id) {
        if (subjectRepository.existsById(id)) {
            subjectRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private SubjectDTO convertToDTO(Subject subject) {
        UUID professorId = (subject.getProfessor() != null) ? subject.getProfessor().getId() : null;

        return new SubjectDTO(
    		subject.getId(),
    		subject.getName(),
    		subject.getCodClass(),
            professorId
        );
    }


}
