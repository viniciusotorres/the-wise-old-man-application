package com.example.the_wise_old_man.service.players;

import com.example.the_wise_old_man.dto.responses.ResponseAuthDTO;
import com.example.the_wise_old_man.service.auth.TokenService;
import com.example.the_wise_old_man.dto.players.PlayerByIdDTO;
import com.example.the_wise_old_man.dto.players.PlayerDTO;
import com.example.the_wise_old_man.dto.responses.ResponseDTO;
import com.example.the_wise_old_man.model.Player;
import com.example.the_wise_old_man.repository.PlayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

@Service
public class PlayersService {

    private static final Map<Integer, String> LEVEL_RANK_MAP = new TreeMap<>();
    private static final int INITIAL_XP_TO_NEXT_LEVEL = 100;
    private static final int INITIAL_INCREMENT = 250;

    static {
        LEVEL_RANK_MAP.put(10, "Bronze");
        LEVEL_RANK_MAP.put(20, "Silver");
        LEVEL_RANK_MAP.put(30, "Gold");
        LEVEL_RANK_MAP.put(40, "Platinum");
        LEVEL_RANK_MAP.put(50, "Diamond");
        LEVEL_RANK_MAP.put(60, "Master");
        LEVEL_RANK_MAP.put(70, "Grandmaster");
        LEVEL_RANK_MAP.put(80, "Challenger");
        LEVEL_RANK_MAP.put(Integer.MAX_VALUE, "Legend");
    }

    private final PlayerRepository playerRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public PlayersService(PlayerRepository playerRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.playerRepository = playerRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponseEntity<ResponseAuthDTO> createPlayer(PlayerDTO player) {
        if (playerRepository.existsByUsername(player.username())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseAuthDTO(null,null,"Já existe jogador com esse nome", HttpStatus.CONFLICT.value()));
        }

        try {
            Player newPlayer = convertToEntity(player);
            playerRepository.save(newPlayer);
            String token = tokenService.generateToken(newPlayer);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseAuthDTO(player.username(),token, "Jogador criado", HttpStatus.CREATED.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseAuthDTO(null,null,"Erro ao criar jogador", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    private Player convertToEntity(PlayerDTO player) {
        Player newPlayer = new Player();
        newPlayer.setUsername(player.username());
        newPlayer.setPassword(passwordEncoder.encode(player.password()));
        newPlayer.setEmail(player.email());
        newPlayer.setXp(0);
        newPlayer.setLevel(1);
        newPlayer.setXpToNextLevel(INITIAL_XP_TO_NEXT_LEVEL);
        newPlayer.setRank("Bronze");
        newPlayer.setCreatedAt(LocalDateTime.parse(player.createdAt()));
        newPlayer.setUpdatedAt(LocalDateTime.parse(player.updatedAt()));
        return newPlayer;
    }

    public ResponseEntity<PlayerByIdDTO> getPlayerById(Long id) {
        return playerRepository.findById(id)
                .map(player -> ResponseEntity.ok(new PlayerByIdDTO(player.getUsername(), player.getEmail(), player.getXp(), player.getLevel(), player.getRank(), player.getXpToNextLevel())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @Transactional
    public ResponseEntity<ResponseDTO> updateXpOfPlayer(Long id, int xp) {
        return playerRepository.findById(id)
                .map(player -> {
                    player.setXp(player.getXp() + xp);
                    updateLevel(player);
                    determineRank(player);
                    playerRepository.save(player);
                    return ResponseEntity.ok(new ResponseDTO("XP atualizado", HttpStatus.OK.value()));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO("Jogador não encontrado", HttpStatus.NOT_FOUND.value())));
    }

    private void updateLevel(Player player) {
        int xp = player.getXp();
        int level = 1;
        int xpToNextLevel = INITIAL_XP_TO_NEXT_LEVEL;
        int increment = INITIAL_INCREMENT;

        while (xp >= xpToNextLevel) {
            level++;
            xp -= xpToNextLevel;
            xpToNextLevel = increment;
            increment += 750;
        }

        player.setLevel(level);
        player.setXpToNextLevel(xpToNextLevel - xp);
    }

    private void determineRank(Player player) {
        int level = player.getLevel();
        for (Map.Entry<Integer, String> entry : LEVEL_RANK_MAP.entrySet()) {
            if (level < entry.getKey()) {
                player.setRank(entry.getValue());
                break;
            }
        }
    }
}