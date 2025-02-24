package com.example.alocacao.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.alocacao.repositories.ProfessorRepository;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return professorRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Professor não encontrado!"));
    }
}
