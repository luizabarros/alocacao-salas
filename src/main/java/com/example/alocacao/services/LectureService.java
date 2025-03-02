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
import com.example.alocacao.entities.Subject;
import com.example.alocacao.repositories.LectureRepository;
import com.example.alocacao.repositories.RoomRepository;
import com.example.alocacao.repositories.SubjectRepository;

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
    
    @Autowired
    private SubjectRepository subjectRepository;

    @Operation(summary = "Criar uma nova aula", description = "Cadastra uma aula respeitando a disponibilidade da sala e os intervalos de 50 minutos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aula criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Horário indisponível ou sala não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public Lecture saveLecture(LectureDTO lectureDTO) {

        Room room = roomRepository.findById(lectureDTO.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada!"));

        Subject subject = subjectRepository.findById(lectureDTO.getSubjectId())
                .orElseThrow(() -> new IllegalArgumentException("Disciplina não encontrada!"));

        LocalTime hourInitLecture = lectureDTO.getHourInit();
        LocalTime hourEndLecture = hourInitLecture.plus(lectureDTO.getDuration());

        // 🔥 Verifica se já existe uma aula na mesma sala, horário, dia e duração (considerando 49 minutos de intervalo)
        boolean existsConflict = lectureRepository.existsByRoomIdAndDayOfWeekAndHourInitAndDuration(
                    lectureDTO.getRoomId(), 
                    lectureDTO.getDayOfWeek(), 
                    hourInitLecture, 
                    lectureDTO.getDuration()
                ) ||
                lectureRepository.existsByRoomIdAndDayOfWeekAndHourInitBetweenAndDuration(
                    lectureDTO.getRoomId(), 
                    lectureDTO.getDayOfWeek(), 
                    hourInitLecture.minusMinutes(49), 
                    hourEndLecture, 
                    lectureDTO.getDuration()
                );

        if (existsConflict) {
            throw new IllegalArgumentException("A sala já está ocupada nesse horário, dia e duração.");
        }

        Lecture lecture = new Lecture();
        lecture.setHourInit(hourInitLecture);
        lecture.setDuration(lectureDTO.getDuration());
        lecture.setRoom(room);
        lecture.setSubject(subject); 
        lecture.setDayOfWeek(lectureDTO.getDayOfWeek());

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
    public Optional<Lecture> updateLecture(UUID id, LectureDTO lectureDTO) {
        Optional<Lecture> existingLectureOpt = lectureRepository.findById(id);
        
        if (existingLectureOpt.isEmpty()) {
            throw new IllegalArgumentException("Aula não encontrada!");
        }

        Lecture existingLecture = existingLectureOpt.get();

        Lecture backupLecture = new Lecture();
        backupLecture.setRoom(existingLecture.getRoom());
        backupLecture.setSubject(existingLecture.getSubject());
        backupLecture.setHourInit(existingLecture.getHourInit());
        backupLecture.setDuration(existingLecture.getDuration());
        backupLecture.setDayOfWeek(existingLecture.getDayOfWeek());
        
        lectureRepository.delete(existingLecture);
        
        Room room = roomRepository.findById(lectureDTO.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada!"));
        Subject subject = subjectRepository.findById(lectureDTO.getSubjectId())
                .orElseThrow(() -> new IllegalArgumentException("Disciplina não encontrada!"));

        LocalTime hourInitLecture = lectureDTO.getHourInit();
        LocalTime hourEndLecture = hourInitLecture.plus(lectureDTO.getDuration());

        boolean existsConflict = lectureRepository.existsByRoomIdAndHourInit(lectureDTO.getRoomId(), hourInitLecture) ||
                lectureRepository.existsByRoomIdAndHourInitBetween(lectureDTO.getRoomId(), hourInitLecture.minusMinutes(49), hourEndLecture);

        if (existsConflict) {
            lectureRepository.save(backupLecture);
            throw new IllegalArgumentException("A sala já está ocupada nesse horário.");
       	
        }

        Lecture updatedLecture = new Lecture();
        updatedLecture.setRoom(room);
        updatedLecture.setSubject(subject);
        updatedLecture.setHourInit(hourInitLecture);
        updatedLecture.setDuration(lectureDTO.getDuration());
        updatedLecture.setDayOfWeek(lectureDTO.getDayOfWeek());
             
        	return Optional.of(lectureRepository.save(updatedLecture));
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

    @Operation(summary = "Converte uma entidade Lecture para DTO", 
            description = "Este método converte um objeto Lecture para LectureDTO, garantindo que os dados estejam estruturados corretamente.")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Conversão bem-sucedida"),
    		@ApiResponse(responseCode = "400", description = "Erro na conversão da entidade")
    })
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