package com.example.alocacao.dtos;

import java.util.UUID;

public class ProfessorDTO {
    private UUID id;
    private String name;
    private String email;
    private boolean confirmed;

    public ProfessorDTO(UUID id, String name, String email, boolean confirmed) {
        this.setId(id);
        this.setName(name);
        this.setEmail(email);
        this.setConfirmed(confirmed);
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
}
