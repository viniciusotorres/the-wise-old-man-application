package com.example.the_wise_old_man.dto.chat;

import com.example.the_wise_old_man.dto.players.PlayerDTO;

import java.util.List;

public record ConversationDTO(
        Long id,
        PlayerDTO player1,
        PlayerDTO player2,
        List<MessageDTO> messages
) {
}
