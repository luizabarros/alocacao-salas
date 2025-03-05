package com.example.alocacao.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

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
@Tag(name = "Professores", description = "Serviço para gerenciamento de professores e autenticação") 
public class ProfessorService {

    @Autowired
    private JWTService jwtUtil;
    
    @Autowired
    private AuthenticationManager authenticationManager; 

    @Autowired
    private JWTService jwtService;

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
        professor.setName(professorRegisterDTO.getName());
        professor.setEmail(professorRegisterDTO.getEmail());
        professor.setIsAdmin(professorRegisterDTO.getIsAdmin());
        professor.setProfessorPassword(encryptedPassword); 
        professor.setConfirmed(false);

        Role roleProfessor = roleRepository.findByRole("ROLE_PROFESSOR")
            .orElseGet(() -> {
                Role newRole = new Role();
                newRole.setRole("ROLE_PROFESSOR");
                return roleRepository.save(newRole);
            });

        professor.setRoles(List.of(roleProfessor));

        Professor savedProfessor = professorRepository.save(professor);
        
        emailProducer.sendEmail(savedProfessor.getEmail());
        savedProfessor.setConfirmed(true);

        return new ProfessorDTO(
            savedProfessor.getId(),
            savedProfessor.getName(),
            savedProfessor.getEmail(),
            savedProfessor.getIsAdmin(),
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
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(), 
                loginDTO.getPassword()
            )
        );

        Professor professor = (Professor) authentication.getPrincipal();

        System.out.println("🔍 ProfessorService - Usuário autenticado com sucesso!");

        String token = jwtService.generateToken(professor.getEmail());
        boolean isAdmin = professor.getIsAdmin();

        return new TokenDTO(token, isAdmin);
    }
    
    @Operation(summary = "Listar professor", description = "Lista professores.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso."),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public List<ProfessorDTO> getAllProfessors() {
        List<Professor> professors = professorRepository.findAll();
        return professors.stream()
                .map(professor -> new ProfessorDTO(professor.getId(), professor.getName(), professor.getEmail(), professor.isConfirmed(), professor.getIsAdmin(), null))
                .collect(Collectors.toList());
    }
    
    @Operation(summary = "Atualizar um professor", description = "Atualiza as informações de um professor existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Professor atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Professor não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @Transactional
    public ProfessorDTO update(UUID id, ProfessorRegisterDTO professorRegisterDTO) {
        Professor professor = professorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Professor não encontrado."));
        
        professor.setName(professorRegisterDTO.getName());
        professor.setEmail(professorRegisterDTO.getEmail());
        professor.setIsAdmin(professorRegisterDTO.getIsAdmin());

        if (professorRegisterDTO.getPassword() != null && !professorRegisterDTO.getPassword().isEmpty()) {
            String encryptedPassword = passwordEncoder.encode(professorRegisterDTO.getPassword());
            professor.setProfessorPassword(encryptedPassword);
        }

        Professor updatedProfessor = professorRepository.save(professor);

        return new ProfessorDTO(
            updatedProfessor.getId(),
            updatedProfessor.getName(),
            updatedProfessor.getEmail(),
            updatedProfessor.getIsAdmin(),
            updatedProfessor.isConfirmed(),
            updatedProfessor.getRoles().stream()
                .map(role -> new RoleDTO(role.getId(), role.getRole()))
                .toList()
        );
    }
    
    @Operation(summary = "Excluir um professor", description = "Exclui um professor existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Professor excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Professor não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @Transactional
    public void delete(UUID id) {
        Professor professor = professorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Professor não encontrado."));

        professorRepository.delete(professor);
    }
}
