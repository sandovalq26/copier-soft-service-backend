package com.copiersoft.backend.services.impl;

import com.copiersoft.backend.dtos.AlmacenDTO;
import com.copiersoft.backend.repositories.AlmacenRepository;
import com.copiersoft.backend.services.AlmacenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlmacenServiceImpl implements AlmacenService {

    @Autowired
    private AlmacenRepository almacenRepository;

    @Override
    public List<AlmacenDTO> getAll() {
        return almacenRepository.findAll().stream()
                .map(almacen -> AlmacenDTO.builder()
                        .codAlmacen(almacen.getCodAlmacen())
                        .nombre(almacen.getNombre())
                        .direccion(almacen.getDireccion())
                        .build())
                .collect(Collectors.toList());
    }
}
