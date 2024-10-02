package com.example.the_wise_old_man.controller.players;

import com.example.the_wise_old_man.dto.players.PlayerByIdDTO;
import com.example.the_wise_old_man.dto.players.PlayerDTO;
import com.example.the_wise_old_man.dto.responses.ResponseAuthDTO;
import com.example.the_wise_old_man.dto.responses.ResponseDTO;
import com.example.the_wise_old_man.model.Player;
import com.example.the_wise_old_man.repository.PlayerRepository;
import com.example.the_wise_old_man.service.players.PlayersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/players")
public class PlayerController {

    @Autowired
    private PlayersService playersService;

    @Autowired
    private PlayerRepository playerRepository;


    @PostMapping("/create")
    public ResponseEntity<ResponseAuthDTO> createPlayer(@Valid @RequestBody PlayerDTO player) {
        return playersService.createPlayer(player);
    }

    @GetMapping("/player/{id}")
    public ResponseEntity<PlayerByIdDTO> getPlayerById(@PathVariable Long id) {
        return playersService.getPlayerById(id);
    }

    @PutMapping("/player/{id}/xp")
    public ResponseEntity<ResponseDTO> updateXpOfPlayer(@PathVariable Long id, @RequestParam int xp) {
        return playersService.updateXpOfPlayer(id, xp);
    }
    @GetMapping("/players")
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }




}
