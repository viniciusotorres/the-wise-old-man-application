package com.example.the_wise_old_man.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResponseAuthDTO(String username, String token, String message, int status) {
}
