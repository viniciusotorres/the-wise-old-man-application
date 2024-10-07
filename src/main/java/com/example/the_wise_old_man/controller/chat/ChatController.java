package com.example.the_wise_old_man.controller.chat;

import com.example.the_wise_old_man.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/news")
    public void broadcastNews(@Payload ChatMessage message) {
        simpMessagingTemplate.convertAndSend("/topic/news", message);
    }
}