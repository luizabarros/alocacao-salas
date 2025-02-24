package com.example.alocacao.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.alocacao.dtos.RoomDTO;
import com.example.alocacao.entities.Room;
import com.example.alocacao.repositories.RoomRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Tag(name = "Salas", description = "Serviço para gerenciamento de salas") 
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Operation(summary = "Criar uma nova sala", description = "Cadastra uma nova sala no sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sala criada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public Room saveRoom(RoomDTO roomDTO) {
        Room room = new Room();
        room.setName(roomDTO.getName());

        return roomRepository.save(room);
    }

    @Operation(summary = "Listar todas as salas", description = "Retorna uma lista com todas as salas cadastradas no sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de salas retornada com sucesso")
    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Operation(summary = "Buscar uma sala por ID", description = "Retorna os detalhes de uma sala específica pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sala encontrada"),
        @ApiResponse(responseCode = "404", description = "Sala não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public Optional<RoomDTO> getRoomById(UUID id) {
        return roomRepository.findById(id).map(this::convertToDTO);
    }

    @Operation(summary = "Atualizar uma sala", description = "Atualiza os dados de uma sala existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sala atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Sala não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public Optional<Room> updateRoom(UUID id, RoomDTO newRoomDTO) {
        return roomRepository.findById(id).map(room -> {
        	room.setName(newRoomDTO.getName());
            return roomRepository.save(room);
        });
    }

    @Operation(summary = "Deletar uma sala", description = "Remove uma sala do sistema pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Sala deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Sala não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public void deleteRoom(UUID id) {
        roomRepository.deleteById(id);
    }

    private RoomDTO convertToDTO(Room room) {
        return new RoomDTO(room.getId(), room.getName());
    }
}
