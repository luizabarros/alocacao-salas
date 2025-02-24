package com.example.alocacao.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.alocacao.entities.Room;

public interface RoomRepository extends JpaRepository<Room, UUID> {

}
