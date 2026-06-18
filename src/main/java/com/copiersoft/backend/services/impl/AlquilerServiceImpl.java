// Erick_Alquileres
package com.copiersoft.backend.services.impl;

import com.copiersoft.backend.dtos.AlquilerDTO;
import com.copiersoft.backend.dtos.AlquilerDropdownDTO;
import com.copiersoft.backend.dtos.CrearAlquilerDTO;
import com.copiersoft.backend.dtos.FotocopiadoraDTO;
import com.copiersoft.backend.models.Alquiler;
import com.copiersoft.backend.models.Cliente;
import com.copiersoft.backend.models.Empleado;
import com.copiersoft.backend.models.Fotocopiadora;
import com.copiersoft.backend.repositories.AlquilerRepository;
import com.copiersoft.backend.repositories.ClienteRepository;
import com.copiersoft.backend.repositories.EmpleadoRepository;
import com.copiersoft.backend.repositories.FotocopiadoraRepository;
import com.copiersoft.backend.services.AlquilerService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlquilerServiceImpl implements AlquilerService {

    private final AlquilerRepository repository;
    private final ClienteRepository clienteRepository;
    private final EmpleadoRepository empleadoRepository;
    private final FotocopiadoraRepository fotocopiadoraRepository;

    public AlquilerServiceImpl(AlquilerRepository repository,
                                ClienteRepository clienteRepository,
                                EmpleadoRepository empleadoRepository,
                                FotocopiadoraRepository fotocopiadoraRepository) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.empleadoRepository = empleadoRepository;
        this.fotocopiadoraRepository = fotocopiadoraRepository;
    }

    @Override
    public List<AlquilerDropdownDTO> getActiveRentals() {
        return repository.findByEstado("EN EJECUCION").stream()
                .map(this::mapToDropdownDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AlquilerDTO> getAll() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AlquilerDTO getById(String id) {
        Alquiler alquiler = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Alquiler no encontrado con cod: " + id));
        return mapToDTO(alquiler);
    }

    @Override
    @Transactional
    public AlquilerDTO create(CrearAlquilerDTO dto) {
        Empleado empleado = empleadoRepository.findById(dto.getCodEmpleado())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Empleado no encontrado: " + dto.getCodEmpleado()));

        Cliente cliente = clienteRepository.findById(dto.getCodCliente())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente no encontrado: " + dto.getCodCliente()));

        Alquiler alquiler = new Alquiler();
        alquiler.setCodAlquiler(generateCodAlquiler());
        alquiler.setEmpleado(empleado);
        alquiler.setCliente(cliente);
        alquiler.setFechaInicio(dto.getFechaInicio());
        alquiler.setFechaFin(dto.getFechaFin());
        alquiler.setPeriodo(dto.getPeriodo());
        alquiler.setValorPeriodo(dto.getValorPeriodo());
        alquiler.setPrecio(dto.getPrecio());
        alquiler.setEstado(dto.getEstado());
        alquiler.setFechaRegistro(LocalDate.now());
        alquiler.setFotocopiadoras(resolveFotocopiadoras(dto.getFotocopiadoras()));

        Alquiler saved = repository.save(alquiler);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public AlquilerDTO update(String id, CrearAlquilerDTO dto) {
        Alquiler alquiler = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Alquiler no encontrado con cod: " + id));

        if (dto.getCodEmpleado() != null) {
            Empleado empleado = empleadoRepository.findById(dto.getCodEmpleado())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Empleado no encontrado: " + dto.getCodEmpleado()));
            alquiler.setEmpleado(empleado);
        }
        if (dto.getCodCliente() != null) {
            Cliente cliente = clienteRepository.findById(dto.getCodCliente())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Cliente no encontrado: " + dto.getCodCliente()));
            alquiler.setCliente(cliente);
        }
        if (dto.getFechaInicio() != null) alquiler.setFechaInicio(dto.getFechaInicio());
        if (dto.getFechaFin() != null) alquiler.setFechaFin(dto.getFechaFin());
        if (dto.getPeriodo() != null) alquiler.setPeriodo(dto.getPeriodo());
        if (dto.getValorPeriodo() != null) alquiler.setValorPeriodo(dto.getValorPeriodo());
        if (dto.getPrecio() != null) alquiler.setPrecio(dto.getPrecio());
        if (dto.getEstado() != null) alquiler.setEstado(dto.getEstado());
        if (dto.getFotocopiadoras() != null) {
            alquiler.setFotocopiadoras(resolveFotocopiadoras(dto.getFotocopiadoras()));
        }

        Alquiler saved = repository.save(alquiler);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Alquiler no encontrado con cod: " + id);
        }
        repository.deleteById(id);
    }

    private List<Fotocopiadora> resolveFotocopiadoras(List<String> codigos) {
        if (codigos == null || codigos.isEmpty()) return new ArrayList<>();
        return codigos.stream()
                .map(cod -> fotocopiadoraRepository.findById(cod)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Fotocopiadora no encontrada: " + cod)))
                .collect(Collectors.toList());
    }

    private String generateCodAlquiler() {
        return repository.findTopByOrderByCodAlquilerDesc()
                .map(last -> {
                    String code = last.getCodAlquiler();
                    int num = Integer.parseInt(code.substring(2)) + 1;
                    return "AQ" + String.format("%04d", num);
                })
                .orElse("AQ0001");
    }

    private AlquilerDropdownDTO mapToDropdownDTO(Alquiler model) {
        String clienteNombre = model.getCliente().getNombres() + " " + model.getCliente().getApellidos();
        return new AlquilerDropdownDTO(model.getCodAlquiler(), clienteNombre, model.getPrecio());
    }

    private AlquilerDTO mapToDTO(Alquiler model) {
        List<FotocopiadoraDTO> fotos = model.getFotocopiadoras().stream()
                .map(f -> FotocopiadoraDTO.builder()
                        .codFotocopiadora(f.getCodFotocopiadora())
                        .nombre(f.getNombre())
                        .marca(f.getMarca())
                        .modelo(f.getModelo())
                        .serie(f.getSerie())
                        .ancho(f.getAncho())
                        .alto(f.getAlto())
                        .fondo(f.getFondo())
                        .build())
                .collect(Collectors.toList());

        return AlquilerDTO.builder()
                .codAlquiler(model.getCodAlquiler())
                .codEmpleado(model.getEmpleado().getCodEmpleado())
                .empleadoNombres(model.getEmpleado().getNombres())
                .empleadoApellidos(model.getEmpleado().getApellidos())
                .codCliente(model.getCliente().getCodCliente())
                .clienteTipoDocumento(model.getCliente().getTipoDocumento())
                .clienteNumeroDocumento(model.getCliente().getNumeroDocumento())
                .clienteNombres(model.getCliente().getNombres())
                .clienteApellidos(model.getCliente().getApellidos())
                .fechaInicio(model.getFechaInicio())
                .fechaFin(model.getFechaFin())
                .periodo(model.getPeriodo())
                .valorPeriodo(model.getValorPeriodo())
                .precio(model.getPrecio())
                .estado(model.getEstado())
                .fechaRegistro(model.getFechaRegistro())
                .fotocopiadoras(fotos)
                .build();
    }
}
