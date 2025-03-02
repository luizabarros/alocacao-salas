package com.example.alocacao.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.alocacao.dtos.AuthenticationData;
import com.example.alocacao.dtos.LoginDTO;
import com.example.alocacao.dtos.TokenDTO;
import com.example.alocacao.entities.Professor;
import com.example.alocacao.services.JWTService;
import com.example.alocacao.services.ProfessorService;

@RestController
@RequestMapping("/public/login")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthenticationController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService tokenService;
    
    @Autowired
    private ProfessorService professorService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginDTO data) {
        try {
            System.out.println("🔍 AuthenticationController - Chamando ProfessorService.login()");
  
            TokenDTO token = professorService.login(data);

            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciais inválidas. Verifique seu email e senha.");
        }
    }
}
