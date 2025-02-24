package com.example.alocacao.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.alocacao.entities.Professor;
import com.example.alocacao.repositories.ProfessorRepository;
import com.example.alocacao.services.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService tokenService;

    @Autowired
    private ProfessorRepository professorRepository;

    public SecurityFilter(JWTService tokenService, ProfessorRepository professorRepository) {
        this.tokenService = tokenService;
        this.professorRepository = professorRepository;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("🔍 Requisição recebida para: " + request.getRequestURI()); // 🔥 Log para depuração

        String requestPath = request.getServletPath();

        // Permite acesso ao login e ao registro de professores
        if (requestPath.equals("/login") || requestPath.startsWith("/professor/public/")) {
            System.out.println("✅ Acesso permitido sem autenticação para: " + requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        String token = recuperarToken(request);

        if (token != null) {
            System.out.println("🔑 Token encontrado, validando...");

            String login = tokenService.getSubject(token);
            Professor professor = professorRepository.findByEmail(login)
                    .orElseThrow(() -> new RuntimeException("❌ Usuário não encontrado"));

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(professor, null, professor.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            System.out.println("⚠️ Nenhum token encontrado na requisição.");
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); 
        }
        return null;
    }
}

