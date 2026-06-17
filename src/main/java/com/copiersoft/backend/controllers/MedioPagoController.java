package com.copiersoft.backend.controllers;

import com.copiersoft.backend.dtos.MedioPagoDTO;
import com.copiersoft.backend.services.MedioPagoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
@CrossOrigin(origins = "*")
public class MedioPagoController {

    private final MedioPagoService service;

    public MedioPagoController(MedioPagoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<MedioPagoDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
