package com.example.alocacao.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.alocacao.dtos.AulaDTO;
import com.example.alocacao.entities.Aula;
import com.example.alocacao.entities.Sala;
import com.example.alocacao.repositories.AulaRepository;
import com.example.alocacao.repositories.SalaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Service
@Tag(name = "Aulas", description = "Serviço para gerenciamento de alocações de aulas") // Define um grupo no Swagger
public class AulaService {

    @Autowired
    private AulaRepository aulaRepository;

    @Autowired
    private SalaRepository salaRepository;

    @Operation(summary = "Criar uma nova aula", description = "Cadastra uma aula respeitando a disponibilidade da sala e os intervalos de 50 minutos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aula criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Horário indisponível ou sala não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public Aula salvarAula(AulaDTO aulaDTO) {
        Sala sala = salaRepository.findById(aulaDTO.getSalaId())
                .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada!"));

        LocalDateTime horaInicioNovaAula = aulaDTO.getHoraInicio();
        LocalDateTime horaFimNovaAula = horaInicioNovaAula.plusMinutes(aulaDTO.getDuracao());

        boolean existeConflito = aulaRepository.existsBySalaIdAndHoraInicioBetween(
                aulaDTO.getSalaId(), horaInicioNovaAula.minusMinutes(49), horaFimNovaAula);

        if (existeConflito) {
            throw new IllegalArgumentException("A sala já está ocupada nesse horário.");
        }

        Aula aula = new Aula();
        aula.setHoraInicio(horaInicioNovaAula);
        aula.setDuracao(aulaDTO.getDuracao());
        aula.setSala(sala);

        return aulaRepository.save(aula);
    }

    @Operation(summary = "Listar todas as aulas", description = "Retorna uma lista com todas as aulas cadastradas no sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de aulas retornada com sucesso")
    public List<AulaDTO> listarAulas() {
        return aulaRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Operation(summary = "Buscar uma aula por ID", description = "Retorna os detalhes de uma aula específica pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aula encontrada"),
        @ApiResponse(responseCode = "404", description = "Aula não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public Optional<AulaDTO> buscarPorId(UUID id) {
        return aulaRepository.findById(id).map(this::convertToDTO);
    }

    @Operation(summary = "Atualizar uma aula", description = "Atualiza os dados de uma aula existente, verificando se a nova alocação respeita os intervalos de 50 minutos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aula atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Horário indisponível"),
        @ApiResponse(responseCode = "404", description = "Aula não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public Optional<Aula> atualizarAula(UUID id, AulaDTO novaAulaDTO) {
        return aulaRepository.findById(id).map(aula -> {

            LocalDateTime novaHoraInicio = novaAulaDTO.getHoraInicio();
            LocalDateTime novaHoraFim = novaHoraInicio.plusMinutes(novaAulaDTO.getDuracao());

            boolean conflito = aulaRepository.existsBySalaIdAndHoraInicioBetween(
                    novaAulaDTO.getSalaId(), novaHoraInicio.minusMinutes(49), novaHoraFim);

            if (conflito) {
                throw new IllegalArgumentException("A sala já está ocupada nesse horário.");
            }

            aula.setHoraInicio(novaHoraInicio);
            aula.setDuracao(novaAulaDTO.getDuracao());
            aula.setDiaDaSemana(novaAulaDTO.getDiaDaSemana());

            salaRepository.findById(novaAulaDTO.getSalaId()).ifPresent(aula::setSala);

            return aulaRepository.save(aula);
        });
    }

    @Operation(summary = "Deletar uma aula", description = "Remove uma aula do sistema pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Aula deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Aula não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public void deletarAula(UUID id) {
        if (!aulaRepository.existsById(id)) {
            throw new IllegalArgumentException("Aula não encontrada!");
        }
        aulaRepository.deleteById(id);
    }

    private AulaDTO convertToDTO(Aula aula) {
        return new AulaDTO(
            aula.getId(),
            aula.getDisciplina() != null ? aula.getDisciplina().getId() : null,
            aula.getSala().getId(),
            aula.getDiaDaSemana(),
            aula.getHoraInicio(),
            aula.getDuracao()
        );
    }
}
