package com.copiersoft.backend.services.impl;

import com.copiersoft.backend.dtos.EmpleadoDTO;
import com.copiersoft.backend.dtos.LoginRequest;
import com.copiersoft.backend.models.Empleado;
import com.copiersoft.backend.repositories.EmpleadoRepository;
import com.copiersoft.backend.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final EmpleadoRepository empleadoRepository;

    @Override
    public EmpleadoDTO login(LoginRequest request) {
        String md5Password = DigestUtils.md5DigestAsHex(request.getContrasena().trim().getBytes());
        Empleado emp = empleadoRepository.findByUsuarioAndContrasena(request.getUsuario(), md5Password)
                .orElseThrow(() -> new RuntimeException("Usuario o contraseña inválidos."));
        
        return EmpleadoDTO.builder()
                .codEmpleado(emp.getCodEmpleado())
                .nombres(emp.getNombres())
                .apellidos(emp.getApellidos())
                .cargo(emp.getCargo())
                .correo(emp.getCorreo())
                .telefono(emp.getTelefono())
                .build();
    }
}
