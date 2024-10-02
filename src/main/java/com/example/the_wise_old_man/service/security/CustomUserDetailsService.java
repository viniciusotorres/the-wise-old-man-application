package com.example.the_wise_old_man.service.security;

import com.example.the_wise_old_man.model.Player;
import com.example.the_wise_old_man.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Player> player = this.playerRepository.findByEmail(username);
        if (player.isEmpty()) {
            throw new UsernameNotFoundException("Jogador não encontrado");
        }
        return new org.springframework.security.core.userdetails.User(player.get().getEmail(), player.get().getPassword(), Collections.emptyList());
    }
}