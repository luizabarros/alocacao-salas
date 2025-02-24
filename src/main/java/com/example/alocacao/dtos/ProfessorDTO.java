package com.example.alocacao.dtos;

import java.util.List;
import java.util.UUID;

public class ProfessorDTO {
    private UUID id;
    private String nome;
    private String email;
    private boolean confirmed;
    private List<RoleDTO> roles;

    public ProfessorDTO(UUID id, String nome, String email, String senha, boolean confirmed, List<RoleDTO> roles) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.confirmed = confirmed;
        this.roles = roles;
    }

    public ProfessorDTO(UUID id, String nome, String email, boolean confirmed, List<RoleDTO> roles) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.confirmed = confirmed;
        this.roles = roles;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
}

