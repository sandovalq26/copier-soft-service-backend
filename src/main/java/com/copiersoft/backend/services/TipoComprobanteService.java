package com.copiersoft.backend.services;

import com.copiersoft.backend.dtos.TipoComprobanteDTO;
import java.util.List;

public interface TipoComprobanteService {
    List<TipoComprobanteDTO> getAll();
}
