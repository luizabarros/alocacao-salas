package com.example.alocacao.repositories;

import java.time.LocalTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.alocacao.entities.Lecture;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, UUID> {

	boolean existsByRoomIdAndHourInit(UUID roomId, LocalTime hourInit);

	boolean existsByRoomIdAndHourInitBetween(UUID roomId, LocalTime minusMinutes, LocalTime hourEndLecture);
}