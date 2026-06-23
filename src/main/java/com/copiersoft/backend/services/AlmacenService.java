package com.copiersoft.backend.services;

import com.copiersoft.backend.dtos.AlmacenDTO;

import java.util.List;

public interface AlmacenService {
    List<AlmacenDTO> getAll();
}
