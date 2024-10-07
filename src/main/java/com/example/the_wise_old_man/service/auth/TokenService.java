package com.example.the_wise_old_man.service.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.the_wise_old_man.exception.TokenValidationException;
import com.example.the_wise_old_man.model.Player;
import com.example.the_wise_old_man.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
@Service
public class TokenService {
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    /**
     * A chave secreta usada para assinar o token JWT.
     * O valor é definido no arquivo application.properties.
     */
    @Value("${api.security.token.secret}")
    private String secret;

    @Autowired
    private PlayerRepository playerRepository;

    /**
     * Gera um token JWT para o usuário.
     *  O token contém o email, username, xp, level, rank, id e xpToNextLevel do jogador.
     * @param player o jogador para o qual o token é gerado
     * @return o token JWT gerado
     */
    public String generateToken(Player player) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("login-auth")
                    .withSubject(player.getEmail())
                    .withClaim("username", player.getUsername())
                    .withClaim("xp", player.getXp())
                    .withClaim("level", player.getLevel())
                    .withClaim("rank", player.getRank())
                    .withClaim("id", player.getId())
                    .withClaim("nextLevel", player.getXpToNextLevel())
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(algorithm);
            logger.info("Token gerado para o usuário: {}", player.getUsername());
            return token;
        } catch (JWTCreationException exception) {
            logger.error("Erro ao gerar token para o usuário: {}", player.getUsername(), exception);
            throw new RuntimeException("Erro ao autenticar usuário", exception);
        }
    }

    /**
     * Valida o token JWT.
     *  Se o token não for válido, retorna null.
     * @param token o token JWT a ser validado
     * @return o assunto (email) do token se válido, null caso contrário
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String subject = JWT.require(algorithm)
                    .withIssuer("login-auth")
                    .build()
                    .verify(token)
                    .getSubject();
            logger.info("Token validado com sucesso para o usuário: {}", subject);
            return subject;
        } catch (JWTVerificationException exception) {
            logger.error("Erro ao validar token: {}", exception.getMessage());
            return null;
        }
    }

    /**
     * Atualiza o token JWT.
     *  Se o token estiver expirado, uma exceção é lançada.
     * @param token o token JWT existente
     * @return um novo token JWT
     */
    public String refreshToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("login-auth")
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);
            Instant expiration = decodedJWT.getExpiresAt().toInstant();
            if (expiration.isBefore(Instant.now())) {
                logger.warn("O token de atualização está expirado");
                throw new TokenValidationException("O token de atualização está expirado");
            }

            String email = decodedJWT.getSubject();
            Player player = findPlayerByEmail(email);
            return generateToken(player);
        } catch (JWTVerificationException exception) {
            logger.error("Erro ao validar token para atualização: {}", exception.getMessage());
            throw new TokenValidationException("Erro ao validar token para atualização", exception);
        }
    }

    /**
     * Encontra um jogador pelo seu email.
     *  Se o jogador não for encontrado, uma exceção é lançada.
     * @param email o email do jogador
     * @return o jogador
     */
    private Player findPlayerByEmail(String email) {
        return playerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    /**
     * Gera a data de expiração para o token.
     *  O token expira em 30 minutos.
     * @return a data de expiração como um Instant
     */
    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.UTC);
    }
}