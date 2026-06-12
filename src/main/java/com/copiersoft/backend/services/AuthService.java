package com.copiersoft.backend.services;

import com.copiersoft.backend.dtos.EmpleadoDTO;
import com.copiersoft.backend.dtos.LoginRequest;

public interface AuthService {
    EmpleadoDTO login(LoginRequest request);
}
