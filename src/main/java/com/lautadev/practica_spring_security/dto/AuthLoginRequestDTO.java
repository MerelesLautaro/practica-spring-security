package com.lautadev.practica_spring_security.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequestDTO(@NotBlank String username,
                                  @NotBlank String password) {
}
