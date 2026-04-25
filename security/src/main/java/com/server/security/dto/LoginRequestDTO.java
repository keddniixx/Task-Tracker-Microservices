package com.server.security.dto;

public record LoginRequestDTO(
        String username,
        String password
) {
}
