package com.copiersoft.backend.services.impl;

import com.copiersoft.backend.dtos.AlquilerRecienteDTO;
import com.copiersoft.backend.dtos.DashboardStatsDTO;
import com.copiersoft.backend.dtos.PagoRecienteDTO;
import com.copiersoft.backend.models.Alquiler;
import com.copiersoft.backend.models.Pago;
import com.copiersoft.backend.repositories.*;
import com.copiersoft.backend.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final EmpleadoRepository empleadoRepository;
    private final ClienteRepository clienteRepository;
    private final FotocopiadoraRepository fotocopiadoraRepository;
    private final AlquilerRepository alquilerRepository;
    private final PagoRepository pagoRepository;

    @Override
    public DashboardStatsDTO getDashboardStats() {
        long totalEmpleados = empleadoRepository.count();
        long totalClientes = clienteRepository.count();
        long totalFotocopiadoras = fotocopiadoraRepository.count();
        long alquileresActivos = alquilerRepository.count();

        List<Alquiler> alquileresRecientes = alquilerRepository.findTop5ByOrderByFechaRegistroDesc();
        List<AlquilerRecienteDTO> alquileresDTO = alquileresRecientes.stream()
                .map(a -> AlquilerRecienteDTO.builder()
                        .codAlquiler(a.getCodAlquiler())
                        .clienteNombres(a.getCliente().getNombres())
                        .clienteApellidos(a.getCliente().getApellidos())
                        .fechaInicio(a.getFechaInicio())
                        .fechaFin(a.getFechaFin())
                        .estado(a.getEstado())
                        .precio(a.getPrecio())
                        .build())
                .collect(Collectors.toList());

        List<Pago> pagosRecientes = pagoRepository.findTop5ByOrderByFechaRegistroDesc();
        List<PagoRecienteDTO> pagosDTO = pagosRecientes.stream()
                .map(p -> PagoRecienteDTO.builder()
                        .codPago(p.getCodPago())
                        .clienteNombres(p.getAlquileres() != null && !p.getAlquileres().isEmpty() ? p.getAlquileres().getFirst().getCliente().getNombres() : "N/A")
                        .clienteApellidos(p.getAlquileres() != null && !p.getAlquileres().isEmpty() ? p.getAlquileres().getFirst().getCliente().getApellidos() : "")
                        .importeTotal(p.getImporteTotal())
                        .medioPagoNombre(p.getMedioPago().getNombre())
                        .build())
                .collect(Collectors.toList());

        return DashboardStatsDTO.builder()
                .totalEmpleados(totalEmpleados)
                .totalClientes(totalClientes)
                .totalFotocopiadoras(totalFotocopiadoras)
                .alquileresActivos(alquileresActivos)
                .alquileresRecientes(alquileresDTO)
                .pagosRecientes(pagosDTO)
                .build();
    }
}
