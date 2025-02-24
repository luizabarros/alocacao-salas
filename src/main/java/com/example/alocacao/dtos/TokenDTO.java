package com.example.alocacao.dtos;

public class TokenDTO {
	private String token;
    private boolean isAdmin;

	public TokenDTO(String token, boolean isAdmin) {
        this.token = token;
        this.isAdmin = isAdmin;
    }
}
