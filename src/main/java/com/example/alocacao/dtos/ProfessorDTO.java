package com.example.alocacao.dtos;

import java.util.List;
import java.util.UUID;

public class ProfessorDTO {
    private UUID id;
    private String name;
    private String email;
    private boolean isAdmin;
    private boolean confirmed;
    private List<RoleDTO> roles;

    public ProfessorDTO(UUID id, String name, String email, String senha, boolean confirmed, boolean isAdmin, List<RoleDTO> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.confirmed = confirmed;
        this.roles = roles;
        this.isAdmin = isAdmin;
    }

    public ProfessorDTO(UUID id, String name, String email, boolean confirmed, boolean isAdmin, List<RoleDTO> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.confirmed = confirmed;
        this.roles = roles;
        this.isAdmin = isAdmin;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }

	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
}

