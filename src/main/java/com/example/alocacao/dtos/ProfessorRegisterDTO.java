package com.example.alocacao.dtos;

public class ProfessorRegisterDTO {
    private String name;
    private String email;
    private String password;
    private boolean isAdmin;
    
    public ProfessorRegisterDTO(String name, String email, String password, boolean isAdmin) {
        this.setName(name);
        this.setEmail(email);
        this.setPassword(password);
        this.setAdmin(isAdmin);
    }

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
}
