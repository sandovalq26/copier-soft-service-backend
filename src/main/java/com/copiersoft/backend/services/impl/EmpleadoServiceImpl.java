package com.copiersoft.backend.services.impl;

import com.copiersoft.backend.dtos.EmpleadoDTO;
import com.copiersoft.backend.enums.TipoDocumento;
import com.copiersoft.backend.models.Empleado;
import com.copiersoft.backend.repositories.EmpleadoRepository;
import com.copiersoft.backend.services.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Override
    public List<EmpleadoDTO> getAll() {
        return empleadoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmpleadoDTO getById(String codEmpleado) {
        Empleado empleado = empleadoRepository.findById(codEmpleado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado"));
        return mapToDTO(empleado);
    }

    @Override
    public EmpleadoDTO create(EmpleadoDTO dto) {
        validarDocumento(dto.getTipoDocumento(), dto.getNumeroDocumento());

        if (empleadoRepository.existsByNumeroDocumento(dto.getNumeroDocumento())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento ya se encuentra registrado.");
        }

        if (empleadoRepository.existsByUsuario(dto.getUsuario())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de usuario ya está en uso.");
        }

        Empleado empleado = new Empleado();
        empleado.setCodEmpleado(""); // El trigger generará el ID
        
        empleado.setTipoDocumento(dto.getTipoDocumento().name());
        empleado.setNumeroDocumento(dto.getNumeroDocumento());
        empleado.setNombres(dto.getNombres());
        empleado.setApellidos(dto.getApellidos());
        empleado.setCargo(dto.getCargo());
        empleado.setTelefono(dto.getTelefono());
        empleado.setCorreo(dto.getCorreo());
        empleado.setUsuario(dto.getUsuario());
        empleado.setFechaContratacion(LocalDate.now());

        // MD5 hash para la contraseña
        if (dto.getContrasena() == null || dto.getContrasena().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña es obligatoria.");
        }
        String md5Hex = DigestUtils.md5DigestAsHex(dto.getContrasena().getBytes());
        empleado.setContrasena(md5Hex);

        empleadoRepository.save(empleado);

        Empleado savedEmpleado = empleadoRepository.findTopByNumeroDocumentoOrderByCodEmpleadoDesc(dto.getNumeroDocumento())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al recuperar el empleado creado"));

        return mapToDTO(savedEmpleado);
    }

    @Override
    public EmpleadoDTO update(String codEmpleado, EmpleadoDTO dto) {
        validarDocumento(dto.getTipoDocumento(), dto.getNumeroDocumento());

        Empleado empleado = empleadoRepository.findById(codEmpleado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado"));

        if (!empleado.getNumeroDocumento().equals(dto.getNumeroDocumento()) &&
                empleadoRepository.existsByNumeroDocumento(dto.getNumeroDocumento())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento ingresado ya está registrado a otro empleado.");
        }

        if (!empleado.getUsuario().equals(dto.getUsuario()) &&
                empleadoRepository.existsByUsuario(dto.getUsuario())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de usuario ya está en uso.");
        }

        empleado.setTipoDocumento(dto.getTipoDocumento().name());
        empleado.setNumeroDocumento(dto.getNumeroDocumento());
        empleado.setNombres(dto.getNombres());
        empleado.setApellidos(dto.getApellidos());
        empleado.setCargo(dto.getCargo());
        empleado.setTelefono(dto.getTelefono());
        empleado.setCorreo(dto.getCorreo());
        empleado.setUsuario(dto.getUsuario());

        // Actualizar contraseña solo si se envió una nueva
        if (dto.getContrasena() != null && !dto.getContrasena().trim().isEmpty()) {
            String md5Hex = DigestUtils.md5DigestAsHex(dto.getContrasena().getBytes());
            empleado.setContrasena(md5Hex);
        }

        Empleado updatedEmpleado = empleadoRepository.save(empleado);
        return mapToDTO(updatedEmpleado);
    }

    private void validarDocumento(TipoDocumento tipo, String numeroDocumento) {
        if (numeroDocumento == null || !numeroDocumento.matches("^[0-9]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El número de documento debe contener solo dígitos.");
        }

        switch (tipo) {
            case DNI:
                if (numeroDocumento.length() != 8) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El DNI debe tener 8 dígitos.");
                }
                break;
            case RUC:
                if (numeroDocumento.length() != 11) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El RUC debe tener 11 dígitos.");
                }
                break;
            case CEX:
                if (numeroDocumento.length() < 8 || numeroDocumento.length() > 15) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El CEX debe tener entre 8 y 15 dígitos.");
                }
                break;
        }
    }

    private EmpleadoDTO mapToDTO(Empleado empleado) {
        EmpleadoDTO dto = EmpleadoDTO.builder()
                .codEmpleado(empleado.getCodEmpleado())
                .numeroDocumento(empleado.getNumeroDocumento())
                .nombres(empleado.getNombres())
                .apellidos(empleado.getApellidos())
                .cargo(empleado.getCargo())
                .telefono(empleado.getTelefono())
                .correo(empleado.getCorreo())
                .usuario(empleado.getUsuario())
                .build();
                
        try {
            dto.setTipoDocumento(TipoDocumento.valueOf(empleado.getTipoDocumento()));
        } catch (IllegalArgumentException | NullPointerException e) {
            dto.setTipoDocumento(null);
        }
        
        return dto;
    }
}
