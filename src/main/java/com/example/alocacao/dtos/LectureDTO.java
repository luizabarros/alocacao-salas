package com.example.alocacao.dtos;

import java.time.Duration;
import java.time.LocalTime;
import java.util.UUID;

import com.example.alocacao.entities.DayOfWeek;

public class LectureDTO {
    private UUID id;
    private UUID subjectId;
    private UUID roomId;
    private DayOfWeek dayOfWeek;
    private LocalTime hourInit;
    private Duration duration;

    public LectureDTO() {}

    public LectureDTO(UUID id, UUID subjectId, UUID roomId, DayOfWeek dayOfWeek, LocalTime hourInit, Duration duration) {
        this.id = id;
        this.subjectId = subjectId;
        this.roomId = roomId;
        this.dayOfWeek = dayOfWeek;
        this.hourInit = hourInit;
        this.duration = duration;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getHourInit() {
        return hourInit;
    }

    public void setHourInit(LocalTime hourInit) {
        this.hourInit = hourInit;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
