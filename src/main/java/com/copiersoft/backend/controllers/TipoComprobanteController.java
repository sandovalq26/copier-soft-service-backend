package com.copiersoft.backend.controllers;

import com.copiersoft.backend.dtos.TipoComprobanteDTO;
import com.copiersoft.backend.services.TipoComprobanteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/receipt-types")
@CrossOrigin(origins = "*")
public class TipoComprobanteController {

    private final TipoComprobanteService service;

    public TipoComprobanteController(TipoComprobanteService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TipoComprobanteDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
