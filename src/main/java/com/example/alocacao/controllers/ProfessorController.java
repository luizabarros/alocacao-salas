package com.example.alocacao.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.alocacao.dtos.LoginDTO;
import com.example.alocacao.dtos.ProfessorDTO;
import com.example.alocacao.dtos.ProfessorRegisterDTO;
import com.example.alocacao.dtos.TokenDTO;
import com.example.alocacao.services.ProfessorService;

@RestController
@RequestMapping("/professor")
public class ProfessorController {
	@Autowired
    private ProfessorService professorService;

	@PostMapping("/public/register")
	public ResponseEntity<ProfessorDTO> register(@RequestBody ProfessorRegisterDTO user) {
	    ProfessorDTO professor = professorService.register(user);
	    return ResponseEntity.status(HttpStatus.CREATED).body(professor);
	}

    @PostMapping("/public/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginRequest) {
        return ResponseEntity.ok(professorService.login(loginRequest));
    }
    
    @GetMapping("/private/all")
    public ResponseEntity<List<ProfessorDTO>> getAllProfessors() {
        List<ProfessorDTO> professors = professorService.getAllProfessors();
        return ResponseEntity.ok(professors);
    }
}
