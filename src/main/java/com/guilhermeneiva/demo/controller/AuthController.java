package com.guilhermeneiva.demo.controller;

import com.guilhermeneiva.demo.config.TokenConfig;
import com.guilhermeneiva.demo.dto.request.LoginRequest;
import com.guilhermeneiva.demo.dto.request.RegisterAdminRequest;
import com.guilhermeneiva.demo.dto.request.RegisterClienteRequest;
import com.guilhermeneiva.demo.dto.response.LoginResponse;
import com.guilhermeneiva.demo.dto.response.RegisterAdminResponse;
import com.guilhermeneiva.demo.dto.response.RegisterClienteResponse;
import com.guilhermeneiva.demo.model.entity.Admin;
import com.guilhermeneiva.demo.model.entity.Usuario;
import com.guilhermeneiva.demo.model.enums.UserRole;
import com.guilhermeneiva.demo.model.repository.AdminRepository;
import com.guilhermeneiva.demo.model.repository.ClienteRepository;
import com.guilhermeneiva.demo.model.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;
    private final ClienteService clienteService;
    private final AdminRepository adminRepository;

    public AuthController(ClienteRepository clienteRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenConfig tokenConfig, ClienteService clienteService, AdminRepository adminRepository) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
        this.clienteService = clienteService;
        this.adminRepository = adminRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.senha());
        Authentication authentication = authenticationManager.authenticate(userAndPass);

        Usuario usuario = (Usuario) authentication.getPrincipal();
        String token = tokenConfig.generateToken(usuario);

        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterClienteResponse> register(@Valid @RequestBody RegisterClienteRequest registerRequest) {
        RegisterClienteResponse response = clienteService.registerAuth(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<RegisterAdminResponse> registerAdmin(@Valid @RequestBody RegisterAdminRequest registerAdminRequest) {
        Admin admin = new Admin();
        admin.setEmail(registerAdminRequest.email());
        admin.setSenha(passwordEncoder.encode(registerAdminRequest.senha()));
        admin.setRole(UserRole.ADMIN);

        adminRepository.save(admin);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterAdminResponse(admin.getEmail(), admin.getId()));

    }
}