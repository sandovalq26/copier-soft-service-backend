// Erick_Alquileres
package com.copiersoft.backend.services.impl;

import com.copiersoft.backend.dtos.FotocopiadoraDTO;
import com.copiersoft.backend.enums.EstadoFotocopiadora;
import com.copiersoft.backend.models.Almacen;
import com.copiersoft.backend.models.Fotocopiadora;
import com.copiersoft.backend.models.Proveedor;
import com.copiersoft.backend.repositories.AlmacenRepository;
import com.copiersoft.backend.repositories.FotocopiadoraRepository;
import com.copiersoft.backend.repositories.ProveedorRepository;
import com.copiersoft.backend.services.FotocopiadoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FotocopiadoraServiceImpl implements FotocopiadoraService {

    @Autowired
    private FotocopiadoraRepository fotocopiadoraRepository;

    @Autowired
    private AlmacenRepository almacenRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    public List<FotocopiadoraDTO> getAll() {
        return fotocopiadoraRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FotocopiadoraDTO getById(String codFotocopiadora) {
        Fotocopiadora foto = fotocopiadoraRepository.findById(codFotocopiadora)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fotocopiadora no encontrada"));
        return mapToDTO(foto);
    }

    @Override
    public FotocopiadoraDTO create(FotocopiadoraDTO dto) {
        validarLimites(dto);

        if (fotocopiadoraRepository.existsBySerie(dto.getSerie())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe una fotocopiadora con esa serie.");
        }

        Almacen almacen = almacenRepository.findById(dto.getCodAlmacen())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Almacén no encontrado."));

        Proveedor proveedor = proveedorRepository.findById(dto.getCodProveedor())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado."));

        Fotocopiadora foto = new Fotocopiadora();
        foto.setCodFotocopiadora(""); // Trigger
        foto.setAlmacen(almacen);
        foto.setProveedor(proveedor);
        foto.setNombre(dto.getNombre());
        foto.setMarca(dto.getMarca());
        foto.setModelo(dto.getModelo());
        foto.setSerie(dto.getSerie());
        foto.setAnioFabricacion(dto.getAnioFabricacion());
        foto.setAncho(dto.getAncho());
        foto.setAlto(dto.getAlto());
        foto.setFondo(dto.getFondo());
        foto.setEstado(dto.getEstado().name());
        foto.setFechaRegistro(LocalDate.now());

        fotocopiadoraRepository.save(foto);

        Fotocopiadora savedFoto = fotocopiadoraRepository.findTopBySerieOrderByCodFotocopiadoraDesc(dto.getSerie())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al recuperar fotocopiadora"));

        return mapToDTO(savedFoto);
    }

    @Override
    public FotocopiadoraDTO update(String codFotocopiadora, FotocopiadoraDTO dto) {
        validarLimites(dto);

        Fotocopiadora foto = fotocopiadoraRepository.findById(codFotocopiadora)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fotocopiadora no encontrada"));

        if (!foto.getSerie().equals(dto.getSerie()) && fotocopiadoraRepository.existsBySerie(dto.getSerie())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe otra fotocopiadora con esa serie.");
        }

        Almacen almacen = almacenRepository.findById(dto.getCodAlmacen())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Almacén no encontrado."));

        Proveedor proveedor = proveedorRepository.findById(dto.getCodProveedor())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado."));

        foto.setAlmacen(almacen);
        foto.setProveedor(proveedor);
        foto.setNombre(dto.getNombre());
        foto.setMarca(dto.getMarca());
        foto.setModelo(dto.getModelo());
        foto.setSerie(dto.getSerie());
        foto.setAnioFabricacion(dto.getAnioFabricacion());
        foto.setAncho(dto.getAncho());
        foto.setAlto(dto.getAlto());
        foto.setFondo(dto.getFondo());
        foto.setEstado(dto.getEstado().name());

        Fotocopiadora updatedFoto = fotocopiadoraRepository.save(foto);
        return mapToDTO(updatedFoto);
    }

    private void validarLimites(FotocopiadoraDTO dto) {
        if (dto.getAnioFabricacion() == null || dto.getAnioFabricacion() < 1 || dto.getAnioFabricacion() > 9999) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Año de fabricación inválido.");
        }
        if (dto.getAncho() == null || dto.getAncho() < 1 || dto.getAncho() > 9999) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ancho inválido.");
        }
        if (dto.getAlto() == null || dto.getAlto() < 1 || dto.getAlto() > 9999) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Alto inválido.");
        }
        if (dto.getFondo() == null || dto.getFondo() < 1 || dto.getFondo() > 9999) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fondo inválido.");
        }
        if (dto.getEstado() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado es requerido.");
        }
    }

    private FotocopiadoraDTO mapToDTO(Fotocopiadora foto) {
        FotocopiadoraDTO dto = FotocopiadoraDTO.builder()
                .codFotocopiadora(foto.getCodFotocopiadora())
                .codAlmacen(foto.getAlmacen().getCodAlmacen())
                .codProveedor(foto.getProveedor().getCodProveedor())
                .nombre(foto.getNombre())
                .marca(foto.getMarca())
                .modelo(foto.getModelo())
                .serie(foto.getSerie())
                .anioFabricacion(foto.getAnioFabricacion())
                .ancho(foto.getAncho())
                .alto(foto.getAlto())
                .fondo(foto.getFondo())
                .build();

        try {
            dto.setEstado(EstadoFotocopiadora.valueOf(foto.getEstado()));
        } catch (IllegalArgumentException | NullPointerException e) {
            dto.setEstado(null);
        }
        return dto;
    }
}
