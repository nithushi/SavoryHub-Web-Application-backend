package lk.jiat.ee.savoryhubproject.controller;

import jakarta.validation.Valid;
import lk.jiat.ee.savoryhubproject.dto.AuthResponseDTO;
import lk.jiat.ee.savoryhubproject.dto.LoginRequestDTO;
import lk.jiat.ee.savoryhubproject.dto.RegisterRequestDTO;
import lk.jiat.ee.savoryhubproject.entity.User;
import lk.jiat.ee.savoryhubproject.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // Base URL for all endpoints in this controller
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Register Endpoint
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            User registeredUser = authService.registerUser(registerRequestDTO);
            return ResponseEntity.ok("User registered successfully! User ID: " + registeredUser.getId());
        } catch (RuntimeException e) {
            // This will catch the "User with this email already exists" error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            // 1. Get the array from the service
            Object[] authResult = authService.loginUser(loginRequestDTO);

            // 2. Unpack the array
            String token = (String) authResult[0];
            User user = (User) authResult[1];

            // 3. Create a new DTO with both token and user, and send it
            return ResponseEntity.ok(new AuthResponseDTO(token, user));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/greet")
    public String greetAuthenticatedUser() {
        return"Hello, Authenticated User! Your token works.";
    }
}