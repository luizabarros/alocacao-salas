package com.example.alocacao.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.alocacao.dtos.LoginDTO;
import com.example.alocacao.dtos.ProfessorDTO;
import com.example.alocacao.dtos.ProfessorRegisterDTO;
import com.example.alocacao.dtos.TokenDTO;
import com.example.alocacao.entities.Professor;
import com.example.alocacao.messaging.EmailProducer;
import com.example.alocacao.repositories.ProfessorRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Service
@Tag(name = "Professores", description = "Serviço para gerenciamento de professores e autenticação") // Define o grupo no Swagger
public class ProfessorService {

    @Autowired
    private JWTService jwtUtil;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailProducer emailProducer;

    @Operation(summary = "Registrar um novo professor", description = "Cria um novo professor e envia um e-mail de confirmação.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Professor registrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "O e-mail já está registrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @Transactional
    public ProfessorDTO register(ProfessorRegisterDTO professorRegisterDTO) {
        if (professorRepository.findByEmail(professorRegisterDTO.getEmail()).isPresent()) {
            throw new RuntimeException("O e-mail já está registrado.");
        }

        String encryptedPassword = passwordEncoder.encode(professorRegisterDTO.getPassword());

        Professor professor = new Professor();
        professor.setName(professorRegisterDTO.getName());
        professor.setEmail(professorRegisterDTO.getEmail());
        professor.setPassword(encryptedPassword);
        professor.setConfirmed(false);

        Professor savedProfessor = professorRepository.save(professor);

        emailProducer.sendEmail(savedProfessor.getEmail());
        savedProfessor.setConfirmed(true);

        return new ProfessorDTO(savedProfessor.getId(), savedProfessor.getName(), savedProfessor.getEmail(), savedProfessor.isConfirmed());
    }

    @Operation(summary = "Autenticar um professor", description = "Realiza o login de um professor e retorna um token JWT.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login bem-sucedido"),
        @ApiResponse(responseCode = "400", description = "E-mail ou senha incorretos"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public TokenDTO login(LoginDTO loginDTO) {
        Professor professor = professorRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), professor.getPassword())) {
            throw new RuntimeException("Senha incorreta");
        }

        String token = jwtUtil.generateToken(loginDTO.getEmail());

        return new TokenDTO(token);
    }
}
