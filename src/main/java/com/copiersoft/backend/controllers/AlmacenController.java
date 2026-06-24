package com.copiersoft.backend.controllers;

import com.copiersoft.backend.dtos.AlmacenDTO;
import com.copiersoft.backend.services.AlmacenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@CrossOrigin(origins = "*")
public class AlmacenController {

    @Autowired
    private AlmacenService almacenService;

    @GetMapping
    public ResponseEntity<List<AlmacenDTO>> getAll() {
        return ResponseEntity.ok(almacenService.getAll());
    }
}
