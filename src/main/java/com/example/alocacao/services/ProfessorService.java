package com.example.alocacao.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.alocacao.dtos.LoginDTO;
import com.example.alocacao.dtos.ProfessorDTO;
import com.example.alocacao.dtos.ProfessorRegisterDTO;
import com.example.alocacao.entities.Professor;
import com.example.alocacao.messaging.EmailProducer;
import com.example.alocacao.repositories.ProfessorRepository;
import com.example.alocacao.dtos.TokenDTO;

@Service
public class ProfessorService {
	@Autowired
    private JWTService jwtUtil;

	@Autowired
    private ProfessorRepository professorRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
    private EmailProducer emailProducer;

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
