package com.example.alocacao.entities;

import jakarta.persistence.*;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

@Entity
public class Room {

	@Id
    @GeneratedValue
    @UuidGenerator
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    private String name;

    public Room() {}

    public Room(String name) {
        this.name = name;
    }

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
    
}

