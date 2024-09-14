package com.example.the_wise_old_man.dto.players;

public record PlayerDTO(Long id, String username, String password,  String email, int xp, int level, int rank, String createdAt, String updatedAt) {
}
