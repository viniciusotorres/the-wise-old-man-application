package com.example.the_wise_old_man.repository;

import com.example.the_wise_old_man.model.Conversation;
import com.example.the_wise_old_man.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByConversation(Conversation conversation);
}
