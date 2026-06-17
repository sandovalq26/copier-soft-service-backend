package com.copiersoft.backend.services.impl;

import com.copiersoft.backend.dtos.PagoDTO;
import com.copiersoft.backend.models.*;
import com.copiersoft.backend.repositories.*;
import com.copiersoft.backend.services.PagoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final AlquilerRepository alquilerRepository;
    private final TipoComprobanteRepository tipoComprobanteRepository;
    private final MedioPagoRepository medioPagoRepository;
    private final FotocopiadoraRepository fotocopiadoraRepository;

    public PagoServiceImpl(PagoRepository pagoRepository, AlquilerRepository alquilerRepository,
                           TipoComprobanteRepository tipoComprobanteRepository, MedioPagoRepository medioPagoRepository,
                           FotocopiadoraRepository fotocopiadoraRepository) {
        this.pagoRepository = pagoRepository;
        this.alquilerRepository = alquilerRepository;
        this.tipoComprobanteRepository = tipoComprobanteRepository;
        this.medioPagoRepository = medioPagoRepository;
        this.fotocopiadoraRepository = fotocopiadoraRepository;
    }

    @Override
    public List<PagoDTO> getAllPagos() {
        return pagoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PagoDTO createPago(PagoDTO dto) {
        Alquiler alquiler = alquilerRepository.findById(dto.getCodAlquiler())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alquiler no encontrado"));

        TipoComprobante tipoComprobante = tipoComprobanteRepository.findById(dto.getCodTipoComprobante())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de Comprobante no encontrado"));

        MedioPago medioPago = medioPagoRepository.findById(dto.getCodMedioPago())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medio de Pago no encontrado"));

        Pago pago = new Pago();
        pago.setCodPago(""); // Trigger generará el ID
        pago.setTipoPago(dto.getTipoPago());
        pago.setTipoComprobante(tipoComprobante);
        pago.setNumeroComprobante(dto.getNumeroComprobante());
        pago.setFormaPago(dto.getFormaPago());
        pago.setMedioPago(medioPago);
        pago.setFechaEmision(dto.getFechaEmision());
        pago.setSubTotal(dto.getSubTotal());
        pago.setDescuento(dto.getDescuento());
        pago.setIgv(dto.getIgv());
        pago.setImporteTotal(dto.getImporteTotal());
        pago.setFechaRegistro(java.time.LocalDate.now());

        pagoRepository.save(pago);

        Pago savedPago = pagoRepository.findTopByNumeroComprobanteOrderByCodPagoDesc(dto.getNumeroComprobante())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al recuperar el pago creado"));

        List<Alquiler> alquileres = new ArrayList<>();
        alquileres.add(alquiler);
        savedPago.setAlquileres(alquileres);
        pagoRepository.save(savedPago);

        // Lógica de Negocio: Pago completo
        if (dto.isPagoCompleto()) {
            alquiler.setEstado("FINALIZADO");
            alquilerRepository.save(alquiler);

            if (alquiler.getFotocopiadoras() != null) {
                for (Fotocopiadora f : alquiler.getFotocopiadoras()) {
                    f.setEstado("DISPONIBLE");
                    fotocopiadoraRepository.save(f);
                }
            }
        }

        return mapToDTO(savedPago);
    }

    private PagoDTO mapToDTO(Pago model) {
        PagoDTO dto = new PagoDTO();
        dto.setCodPago(model.getCodPago());
        dto.setTipoPago(model.getTipoPago());
        dto.setNumeroComprobante(model.getNumeroComprobante());
        dto.setFormaPago(model.getFormaPago());
        dto.setFechaEmision(model.getFechaEmision());
        dto.setSubTotal(model.getSubTotal());
        dto.setDescuento(model.getDescuento());
        dto.setIgv(model.getIgv());
        dto.setImporteTotal(model.getImporteTotal());

        if (model.getTipoComprobante() != null) {
            dto.setCodTipoComprobante(model.getTipoComprobante().getCodTipoComprobante());
            dto.setComprobanteNombre(model.getTipoComprobante().getNombre());
        }

        if (model.getMedioPago() != null) {
            dto.setCodMedioPago(model.getMedioPago().getCodMedioPago());
            dto.setMedioPagoNombre(model.getMedioPago().getNombre());
        }

        if (model.getAlquileres() != null && !model.getAlquileres().isEmpty()) {
            Alquiler a = model.getAlquileres().get(0);
            dto.setCodAlquiler(a.getCodAlquiler());
            if (a.getCliente() != null) {
                dto.setClienteDocumento(a.getCliente().getTipoDocumento() + ": " + a.getCliente().getNumeroDocumento());
                dto.setClienteNombres(a.getCliente().getNombres() + " " + a.getCliente().getApellidos());
            }
        }

        return dto;
    }
}
