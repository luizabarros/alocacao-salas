package com.example.alocacao.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.alocacao.dtos.LoginDTO;
import com.example.alocacao.dtos.ProfessorDTO;
import com.example.alocacao.dtos.ProfessorRegisterDTO;
import com.example.alocacao.dtos.RoleDTO;
import com.example.alocacao.dtos.TokenDTO;
import com.example.alocacao.entities.Professor;
import com.example.alocacao.entities.Role;
import com.example.alocacao.messaging.EmailProducer;
import com.example.alocacao.repositories.ProfessorRepository;
import com.example.alocacao.repositories.RoleRepository; 

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
    private RoleRepository roleRepository; 

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
        professor.setNome(professorRegisterDTO.getName());
        professor.setEmail(professorRegisterDTO.getEmail());
        professor.setSenha(encryptedPassword); 
        professor.setConfirmed(false);

        Role roleProfessor = roleRepository.findByRole("ROLE_PROFESSOR")
            .orElseGet(() -> {
                Role newRole = new Role();
                newRole.setRole("ROLE_PROFESSOR");
                return roleRepository.save(newRole);
            });

        professor.setRoles(List.of(roleProfessor));

        Professor savedProfessor = professorRepository.save(professor);

        return new ProfessorDTO(
            savedProfessor.getId(),
            savedProfessor.getNome(),
            savedProfessor.getEmail(),
            savedProfessor.isConfirmed(),
            savedProfessor.getRoles().stream()
                .map(role -> new RoleDTO(role.getId(), role.getRole()))
                .toList()
        );
    }


    @Operation(summary = "Autenticar um professor", description = "Realiza o login de um professor e retorna um token JWT.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login bem-sucedido"),
        @ApiResponse(responseCode = "400", description = "E-mail ou senha incorretos"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    
    public TokenDTO login(LoginDTO loginDTO) {
        System.out.println("📩 Recebendo tentativa de login para: " + loginDTO.getEmail());

        Professor professor = professorRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> {
                    System.out.println("❌ Usuário não encontrado: " + loginDTO.getEmail());
                    return new RuntimeException("Professor não encontrado");
                });

        System.out.println("✅ Professor encontrado: " + professor.getEmail());

        boolean senhaCorreta = passwordEncoder.matches(loginDTO.getPassword(), professor.getSenha());
        System.out.println("🔑 Senha correta? " + senhaCorreta);

        if (!senhaCorreta) {
            throw new RuntimeException("Senha incorreta");
        }

        String token = jwtUtil.generateToken(loginDTO.getEmail());
        System.out.println("🎟 Token gerado: " + token);

        return new TokenDTO(token);
    }

}
