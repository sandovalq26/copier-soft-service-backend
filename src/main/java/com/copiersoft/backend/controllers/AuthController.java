package com.copiersoft.backend.controllers;

import com.copiersoft.backend.dtos.EmpleadoDTO;
import com.copiersoft.backend.dtos.LoginRequest;
import com.copiersoft.backend.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<EmpleadoDTO> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).build();
        }
    }
}
