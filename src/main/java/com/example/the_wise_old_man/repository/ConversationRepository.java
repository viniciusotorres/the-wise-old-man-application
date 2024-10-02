package com.example.the_wise_old_man.repository;

import com.example.the_wise_old_man.model.Conversation;
import com.example.the_wise_old_man.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByPlayer1AndPlayer2(Player player1, Player player2);
    List<Conversation> findByPlayer1OrPlayer2(Player player1, Player player2);

    Optional<Conversation> getConversationById(Long id);
}
