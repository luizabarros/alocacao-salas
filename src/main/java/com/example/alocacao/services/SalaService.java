package com.example.alocacao.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.alocacao.dtos.SalaDTO;
import com.example.alocacao.entities.Sala;
import com.example.alocacao.repositories.SalaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Tag(name = "Salas", description = "Serviço para gerenciamento de salas") // Define um grupo no Swagger UI
public class SalaService {

    @Autowired
    private SalaRepository salaRepository;

    @Operation(summary = "Criar uma nova sala", description = "Cadastra uma nova sala no sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sala criada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public Sala salvarSala(SalaDTO salaDTO) {
        Sala sala = new Sala();
        sala.setNome(salaDTO.getNome());

        return salaRepository.save(sala);
    }

    @Operation(summary = "Listar todas as salas", description = "Retorna uma lista com todas as salas cadastradas no sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de salas retornada com sucesso")
    public List<SalaDTO> listarSalas() {
        return salaRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Operation(summary = "Buscar uma sala por ID", description = "Retorna os detalhes de uma sala específica pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sala encontrada"),
        @ApiResponse(responseCode = "404", description = "Sala não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public Optional<SalaDTO> buscarPorId(UUID id) {
        return salaRepository.findById(id).map(this::convertToDTO);
    }

    @Operation(summary = "Atualizar uma sala", description = "Atualiza os dados de uma sala existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sala atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Sala não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public Optional<Sala> atualizarSala(UUID id, SalaDTO novaSalaDTO) {
        return salaRepository.findById(id).map(sala -> {
            sala.setNome(novaSalaDTO.getNome());
            return salaRepository.save(sala);
        });
    }

    @Operation(summary = "Deletar uma sala", description = "Remove uma sala do sistema pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Sala deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Sala não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public void deletarSala(UUID id) {
        salaRepository.deleteById(id);
    }

    private SalaDTO convertToDTO(Sala sala) {
        return new SalaDTO(sala.getId(), sala.getNome());
    }
}
