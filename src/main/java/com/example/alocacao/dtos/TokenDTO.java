package com.example.alocacao.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenDTO {
    
    @JsonProperty("token")
    private String token;
    
    @JsonProperty("isAdmin")
    private boolean isAdmin;

    public TokenDTO(String token, boolean isAdmin) {
        this.token = token;
        this.isAdmin = isAdmin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
