package com.copiersoft.backend.controllers;

import com.copiersoft.backend.dtos.AlquilerDropdownDTO;
import com.copiersoft.backend.services.AlquilerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@CrossOrigin(origins = "*")
public class AlquilerController {

    private final AlquilerService service;

    public AlquilerController(AlquilerService service) {
        this.service = service;
    }

    @GetMapping("/active")
    public ResponseEntity<List<AlquilerDropdownDTO>> getActiveRentals() {
        return ResponseEntity.ok(service.getActiveRentals());
    }
}
