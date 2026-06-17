package com.copiersoft.backend.services;

import com.copiersoft.backend.dtos.MedioPagoDTO;
import java.util.List;

public interface MedioPagoService {
    List<MedioPagoDTO> getAll();
}
