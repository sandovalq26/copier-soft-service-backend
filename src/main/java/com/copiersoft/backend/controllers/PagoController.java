package com.copiersoft.backend.controllers;

import com.copiersoft.backend.dtos.PagoDTO;
import com.copiersoft.backend.services.PagoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PagoController {

    private final PagoService service;

    public PagoController(PagoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PagoDTO>> getAll() {
        return ResponseEntity.ok(service.getAllPagos());
    }

    @PostMapping
    public ResponseEntity<PagoDTO> create(@RequestBody PagoDTO dto) {
        PagoDTO created = service.createPago(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
