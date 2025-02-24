package com.example.alocacao.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.alocacao.dtos.DadosAutenticacao;
import com.example.alocacao.dtos.TokenDTO;
import com.example.alocacao.entities.Professor;
import com.example.alocacao.services.JWTService;

@RestController
@RequestMapping("/login")
//@Secured("ROLE_PROFESSOR")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService tokenService;

    @PostMapping
    public ResponseEntity<TokenDTO> efetuarLogin(@RequestBody DadosAutenticacao dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(
            dados.login(),  
            dados.senha()   
        );

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        Professor professor = (Professor) authentication.getPrincipal();
        String jwt = tokenService.generateToken(professor.getEmail());

        return ResponseEntity.ok(new TokenDTO(jwt));
    }

}
