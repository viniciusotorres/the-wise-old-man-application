package com.example.the_wise_old_man.service.auth;

import com.example.the_wise_old_man.dto.players.PlayerAuthDTO;
import com.example.the_wise_old_man.dto.responses.ResponseAuthDTO;
import com.example.the_wise_old_man.exception.AuthenticationException;
import com.example.the_wise_old_man.repository.PlayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(PlayerRepository playerRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public ResponseEntity<ResponseAuthDTO> login(PlayerAuthDTO playerLoginDTO) {
        var player = playerRepository.findByEmail(playerLoginDTO.email())
                .orElseThrow(() -> new AuthenticationException("Player Not Found"));

        if (passwordEncoder.matches(playerLoginDTO.password(), player.getPassword())) {
            var token = tokenService.generateToken(player);
            return ResponseEntity.ok(new ResponseAuthDTO(player.getUsername(), token, "Login efetuado com sucesso", HttpStatus.OK.value()));
        }

        throw new AuthenticationException("Invalid credentials");
    }
}