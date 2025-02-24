package com.example.alocacao.entities;

import jakarta.persistence.*;
import java.time.LocalTime;
import java.time.Duration;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

@Entity
public class Lecture {

	@Id
    @GeneratedValue
    @UuidGenerator
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Enumerated(EnumType.STRING) 
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @Column(name = "hour_init")
    private LocalTime hourInit;
    private Duration duration;

    public Lecture() {}

    public Lecture(Subject subject, Room room, DayOfWeek dayOfWeek, LocalTime hourInit, Duration duration) {
        this.subject = subject;
        this.room = room;
        this.dayOfWeek = dayOfWeek;
        this.hourInit = hourInit;
        this.duration = duration;
    }

    public UUID getId() {
        return id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
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
