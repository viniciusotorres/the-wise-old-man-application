package com.example.the_wise_old_man.dto.chat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record MessageDTO(
        String content,
        String sender,
        String recipientUsername,
        String timestamp
) {
    public MessageDTO(String content, String sender, String recipientUsername, LocalDateTime timestamp) {
        this(content, sender, recipientUsername, timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
