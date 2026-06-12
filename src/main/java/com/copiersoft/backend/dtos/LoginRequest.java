package com.copiersoft.backend.dtos;

import lombok.Data;

@Data
public class LoginRequest {
    private String usuario;
    private String contrasena;
}
