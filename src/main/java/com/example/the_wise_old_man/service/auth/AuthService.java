package com.example.the_wise_old_man.service.auth;

import com.example.the_wise_old_man.dto.players.PlayerAuthDTO;
import com.example.the_wise_old_man.dto.responses.ResponseAuthDTO;
import com.example.the_wise_old_man.exception.AuthenticationException;
import com.example.the_wise_old_man.repository.PlayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AuthService {
    private static final Logger logger = Logger.getLogger(AuthService.class.getName());

    /**
     * O repositório de jogadores.
     */
    private final PlayerRepository playerRepository;
    /**
     * O codificador de senhas.
     */
    private final PasswordEncoder passwordEncoder;
    /**
     * O serviço de tokens.
     */
    private final TokenService tokenService;

    public AuthService(PlayerRepository playerRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    /**
     * Realiza o login do jogador.
     *
     * @param playerLoginDTO o objeto que contém as credenciais do jogador
     * @return uma resposta com os dados de autenticação
     * @throws AuthenticationException se as credenciais forem inválidas
     */
    public ResponseEntity<ResponseAuthDTO> login(PlayerAuthDTO playerLoginDTO) {
        var player = playerRepository.findByEmail(playerLoginDTO.email())
                .orElseThrow(() -> {
                    logger.warning("Jogador não encontrado: " + playerLoginDTO.email());
                    return new AuthenticationException("Jogador não encontrado");
                });

        if (isPasswordValid(playerLoginDTO.password(), player.getPassword())) {
            var token = tokenService.generateToken(player);
            logger.info("Login efetuado com sucesso para o usuário: " + player.getUsername());
            return ResponseEntity.ok(new ResponseAuthDTO(player.getUsername(), token, "Login efetuado com sucesso", HttpStatus.OK.value()));
        }

        logger.warning("Credenciais inválidas para o usuário: " + playerLoginDTO.email());
        throw new AuthenticationException("Credenciais inválidas");
    }

    /**
     * Verifica se a senha fornecida corresponde à senha armazenada.
     *
     * @param rawPassword a senha fornecida
     * @param encodedPassword a senha codificada armazenada
     * @return true se a senha corresponder, false caso contrário
     */
    private boolean isPasswordValid(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
