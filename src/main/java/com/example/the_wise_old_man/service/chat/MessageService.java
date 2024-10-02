package com.example.the_wise_old_man.service.chat;

import com.example.the_wise_old_man.model.Conversation;
import com.example.the_wise_old_man.model.Message;
import com.example.the_wise_old_man.model.Player;
import com.example.the_wise_old_man.repository.ConversationRepository;
import com.example.the_wise_old_man.repository.MessageRepository;
import com.example.the_wise_old_man.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public Conversation findConversationById(Long id) {
        return conversationRepository.findById(id).orElseThrow(() -> new RuntimeException("Conversation not found"));
    }

    public Player findPlayerByUsername(String username) {
        return playerRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("Player not found"));
    }

    public Conversation findOrCreateConversation(Player sender, String recipientUsername) {
        Player recipient = findPlayerByUsername(recipientUsername);
        Optional<Conversation> conversationOpt = conversationRepository.findByPlayer1AndPlayer2(sender, recipient);
        if (conversationOpt.isEmpty()) {
            conversationOpt = conversationRepository.findByPlayer1AndPlayer2(recipient, sender);
        }
        return conversationOpt.orElseGet(() -> createConversation(sender, recipient));
    }

    public Conversation createConversation(Player player1, Player player2) {
        Conversation conversation = new Conversation();
        conversation.setPlayer1(player1);
        conversation.setPlayer2(player2);
        return conversationRepository.save(conversation);
    }

    public Message sendMessage(Player sender, Conversation conversation, String content) {
        Player recipient = conversation.getPlayer1().equals(sender) ? conversation.getPlayer2() : conversation.getPlayer1();
        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setConversation(conversation);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }
}