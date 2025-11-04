package com.amigosecreto.service;

import com.amigosecreto.dto.request.LoginRequest;
import com.amigosecreto.dto.request.RegisterRequest;
import com.amigosecreto.dto.response.AuthResponse;
import com.amigosecreto.model.User;
import com.amigosecreto.repository.UserRepository;
import com.amigosecreto.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service para operações de autenticação.
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Registra um novo usuário.
     *
     * @param request Dados do usuário
     * @return Resposta com token JWT
     * @throws RuntimeException se username ou email já existirem
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Verificar se username já existe
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username já está em uso");
        }

        // Verificar se email já existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }

        // Criar novo usuário
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        // Gerar token
        String token = jwtTokenProvider.generateTokenFromUsername(user.getUsername());

        return new AuthResponse(token, user.getUsername(), user.getEmail());
    }

    /**
     * Realiza login do usuário.
     *
     * @param request Credenciais do usuário
     * @return Resposta com token JWT
     */
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return new AuthResponse(token, user.getUsername(), user.getEmail());
    }
}

