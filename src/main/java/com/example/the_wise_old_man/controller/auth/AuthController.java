package com.example.the_wise_old_man.controller.auth;

import com.example.the_wise_old_man.dto.players.PlayerAuthDTO;
import com.example.the_wise_old_man.dto.responses.ResponseAuthDTO;
import com.example.the_wise_old_man.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<ResponseAuthDTO> login(@RequestBody PlayerAuthDTO playerLoginDTO) {
    return authService.login(playerLoginDTO);
    }
}