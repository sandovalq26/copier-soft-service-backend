package com.copiersoft.backend.controllers;

import com.copiersoft.backend.dtos.EmpleadoDTO;
import com.copiersoft.backend.services.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public ResponseEntity<List<EmpleadoDTO>> getAll() {
        return ResponseEntity.ok(empleadoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(empleadoService.getById(id));
    }

    @PostMapping
    public ResponseEntity<EmpleadoDTO> create(@RequestBody EmpleadoDTO empleadoDTO) {
        EmpleadoDTO created = empleadoService.create(empleadoDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> update(@PathVariable String id, @RequestBody EmpleadoDTO empleadoDTO) {
        return ResponseEntity.ok(empleadoService.update(id, empleadoDTO));
    }
}
