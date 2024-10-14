package za.ac.cput.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.dto.request.LoginRequest;
import za.ac.cput.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    private final AuthenticationService authService;

    public AuthController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Object user = authService.authenticate(request.getEmail(), request.getPassword(), request.getRole());
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
    }
}