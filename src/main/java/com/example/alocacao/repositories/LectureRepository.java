package com.example.alocacao.repositories;

import java.time.Duration;
import java.time.LocalTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.alocacao.entities.DayOfWeek;
import com.example.alocacao.entities.Lecture;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, UUID> {

	boolean existsByRoomIdAndHourInit(UUID roomId, LocalTime hourInit);

	boolean existsByRoomIdAndHourInitBetween(UUID roomId, LocalTime minusMinutes, LocalTime hourEndLecture);

	@Query("SELECT COUNT(l) > 0 FROM Lecture l " +
		       "WHERE l.room.id = :roomId " + 
		       "AND l.id <> :lectureId " + 
		       "AND l.dayOfWeek = :dayOfWeek " + 
		       "AND (" +
		       "    (:hourInit BETWEEN l.hourInit AND FUNCTION('TIME', l.hourInit + l.duration)) OR " + 
		       "    (:hourEnd BETWEEN l.hourInit AND FUNCTION('TIME', l.hourInit + l.duration)) OR " + 
		       "    (l.hourInit BETWEEN :hourInit AND :hourEnd) " + 
		       ")")
		boolean existsByRoomDayAndHourExcludingLecture(
		        @Param("roomId") UUID roomId,
		        @Param("dayOfWeek") DayOfWeek dayOfWeek,
		        @Param("hourInit") LocalTime hourInit,
		        @Param("hourEnd") LocalTime hourEnd,
		        @Param("lectureId") UUID lectureId);

	boolean existsByRoomIdAndDayOfWeekAndHourInitBetweenAndDuration(UUID roomId, DayOfWeek dayOfWeek,
			LocalTime minusMinutes, LocalTime hourEndLecture, Duration duration);

	boolean existsByRoomIdAndDayOfWeekAndHourInitAndDuration(UUID roomId, DayOfWeek dayOfWeek,
			LocalTime hourInitLecture, Duration duration);

}