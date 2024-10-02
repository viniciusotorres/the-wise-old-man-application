package com.example.the_wise_old_man.dto.players;

public record PlayerByIdDTO(String username, String email, int xp, int level, String rank, int xpToNextLevel) {
}
