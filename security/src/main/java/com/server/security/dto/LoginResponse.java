package com.server.security.dto;

import lombok.Builder;

@Builder
public record LoginResponse(
        String token
) {
}
