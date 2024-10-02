package com.example.the_wise_old_man.controller.chat;

import com.example.the_wise_old_man.dto.chat.MessageDTO;
import com.example.the_wise_old_man.model.Conversation;
import com.example.the_wise_old_man.model.Message;
import com.example.the_wise_old_man.model.Notification;
import com.example.the_wise_old_man.model.Player;
import com.example.the_wise_old_man.service.chat.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(MessageDTO messageDTO) {
        Player sender = messageService.findPlayerByUsername(messageDTO.sender());
        Player recipient = messageService.findPlayerByUsername(messageDTO.recipientUsername());
        Conversation conversation = messageService.findOrCreateConversation(sender, recipient.getEmail());
        Message message = messageService.sendMessage(sender, conversation, messageDTO.content());
        messagingTemplate.convertAndSend("/topic/messages/" + conversation.getId(), messageDTO);
    }


    @MessageMapping("/chat.sendNotification")
    @SendTo("/topic/notification")
    public Notification sendNotification(Notification notification) {
        return notification;
    }


}
