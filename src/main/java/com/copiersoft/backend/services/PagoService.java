package com.copiersoft.backend.services;

import com.copiersoft.backend.dtos.PagoDTO;
import java.util.List;

public interface PagoService {
    List<PagoDTO> getAllPagos();
    PagoDTO createPago(PagoDTO dto);
}
