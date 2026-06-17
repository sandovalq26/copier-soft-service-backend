package com.copiersoft.backend.services;

import com.copiersoft.backend.dtos.AlquilerDropdownDTO;
import java.util.List;

public interface AlquilerService {
    List<AlquilerDropdownDTO> getActiveRentals();
}
