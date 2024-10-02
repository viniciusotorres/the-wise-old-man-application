package com.example.the_wise_old_man.controller.chat;

import com.example.the_wise_old_man.dto.chat.ConversationDTO;
import com.example.the_wise_old_man.dto.chat.MessageDTO;
import com.example.the_wise_old_man.dto.players.PlayerDTO;
import com.example.the_wise_old_man.model.Conversation;
import com.example.the_wise_old_man.model.Message;
import com.example.the_wise_old_man.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat")
public class ConversationController {

    @Autowired
    private ConversationRepository conversationRepository;

    @GetMapping("/conversations")
    public List<Conversation> getAllConversations() {
        return conversationRepository.findAll();
    }

    @GetMapping("/conversations/{id}/messages")
    public Optional<ConversationDTO> getConversationById(@PathVariable Long id) {
        return conversationRepository.findById(id).map(this::convertToDTO);
    }

    private ConversationDTO convertToDTO(Conversation conversation) {
        List<MessageDTO> messageDTOs = conversation.getMessages().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        PlayerDTO player1DTO = new PlayerDTO(
                conversation.getPlayer1().getId(),
                conversation.getPlayer1().getUsername(),
                conversation.getPlayer1().getPassword(),
                conversation.getPlayer1().getEmail(),
                conversation.getPlayer1().getXp(),
                conversation.getPlayer1().getLevel(),
                conversation.getPlayer1().getRank(),
                conversation.getPlayer1().getCreatedAt().toString(),
                conversation.getPlayer1().getUpdatedAt().toString()
        );

        PlayerDTO player2DTO = new PlayerDTO(
                conversation.getPlayer2().getId(),
                conversation.getPlayer2().getUsername(),
                conversation.getPlayer2().getPassword(),
                conversation.getPlayer2().getEmail(),
                conversation.getPlayer2().getXp(),
                conversation.getPlayer2().getLevel(),
                conversation.getPlayer2().getRank(),
                conversation.getPlayer2().getCreatedAt().toString(),
                conversation.getPlayer2().getUpdatedAt().toString()
        );

        return new ConversationDTO(conversation.getId(), player1DTO, player2DTO, messageDTOs);
    }

    private MessageDTO convertToDTO(Message message) {
        return new MessageDTO(
                message.getContent(),
                message.getSender().getUsername(), // Corrected to get the sender's username
                message.getRecipient().getUsername(), // Added to get the recipient's username
                message.getTimestamp().toString()
        );
    }
}