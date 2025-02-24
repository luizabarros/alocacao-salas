package com.example.alocacao.services;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.alocacao.dtos.LectureDTO;
import com.example.alocacao.entities.Lecture;
import com.example.alocacao.entities.Room;
import com.example.alocacao.repositories.LectureRepository;
import com.example.alocacao.repositories.RoomRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Service
@Tag(name = "Aulas", description = "Serviço para gerenciamento de alocações de aulas") // Define um grupo no Swagger
public class LectureService {

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Operation(summary = "Criar uma nova aula", description = "Cadastra uma aula respeitando a disponibilidade da sala e os intervalos de 50 minutos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aula criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Horário indisponível ou sala não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public Lecture saveLecture(LectureDTO lectureDTO) {
        Room room = roomRepository.findById(lectureDTO.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada!"));

        LocalTime hourInitLecture = lectureDTO.getHourInit();
        LocalTime hourEndLecture = hourInitLecture.plus(lectureDTO.getDuration());

        boolean existsConflict = lectureRepository.existsByRoomIdAndHourInitBetween(
                lectureDTO.getRoomId(), hourInitLecture.minusMinutes(49), hourEndLecture);

        if (existsConflict) {
            throw new IllegalArgumentException("A sala já está ocupada nesse horário.");
        }

        Lecture lecture = new Lecture();
        lecture.setHourInit(hourInitLecture);
        lecture.setDuration(lectureDTO.getDuration());
        lecture.setRoom(room);

        return lectureRepository.save(lecture);
    }

    @Operation(summary = "Listar todas as aulas", description = "Retorna uma lista com todas as aulas cadastradas no sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de aulas retornada com sucesso")
    public List<LectureDTO> listLectures() {
        return lectureRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Operation(summary = "Buscar uma aula por ID", description = "Retorna os detalhes de uma aula específica pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aula encontrada"),
        @ApiResponse(responseCode = "404", description = "Aula não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public Optional<LectureDTO> getByLectureId(UUID id) {
        return lectureRepository.findById(id).map(this::convertToDTO);
    }

    @Operation(summary = "Atualizar uma aula", description = "Atualiza os dados de uma aula existente, verificando se a nova alocação respeita os intervalos de 50 minutos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aula atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Horário indisponível"),
        @ApiResponse(responseCode = "404", description = "Aula não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public Optional<Lecture> updateLecture(UUID id, LectureDTO newLectureDTO) {
        return lectureRepository.findById(id).map(lecture -> {

            LocalTime newHourInit = newLectureDTO.getHourInit();
            LocalTime newHourEnd = newHourInit.plus(newLectureDTO.getDuration());

            boolean existsConflict = lectureRepository.existsByRoomIdAndHourInitBetween(
            		newLectureDTO.getRoomId(), newHourInit.minusMinutes(49), newHourEnd);

            if (existsConflict) {
                throw new IllegalArgumentException("A sala já está ocupada nesse horário.");
            }

            lecture.setHourInit(newHourInit);
            lecture.setDuration(newLectureDTO.getDuration());
            lecture.setDayOfWeek(newLectureDTO.getDayOfWeek());

            roomRepository.findById(newLectureDTO.getRoomId()).ifPresent(lecture::setRoom);

            return lectureRepository.save(lecture);
        });
    }

    @Operation(summary = "Deletar uma aula", description = "Remove uma aula do sistema pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Aula deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Aula não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public void deleteLecture(UUID id) {
        if (!lectureRepository.existsById(id)) {
            throw new IllegalArgumentException("Aula não encontrada!");
        }
        lectureRepository.deleteById(id);
    }

    private LectureDTO convertToDTO(Lecture lecture) {
        return new LectureDTO(
            lecture.getId(),
            lecture.getSubject() != null ? lecture.getSubject().getId() : null,
            lecture.getRoom().getId(),
            lecture.getDayOfWeek(),
            lecture.getHourInit(),
            lecture.getDuration()
        );
    }
} 