package com.copiersoft.backend.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashboardStatsDTO {
    private long totalEmpleados;
    private long totalClientes;
    private long totalFotocopiadoras;
    private long alquileresActivos;
    private List<AlquilerRecienteDTO> alquileresRecientes;
    private List<PagoRecienteDTO> pagosRecientes;
}
