package com.example.alocacao.entities;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import com.example.alocacao.dtos.RoleDTO;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String role;

    public Role() {}

    public Role(RoleDTO roleDTO) {
        this.id = roleDTO.id();
        this.role = roleDTO.role();
    }

    @Override
    public String getAuthority() {
        return this.role; 
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

