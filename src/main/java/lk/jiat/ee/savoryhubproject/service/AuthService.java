package lk.jiat.ee.savoryhubproject.service;

import lk.jiat.ee.savoryhubproject.dto.LoginRequestDTO;
import lk.jiat.ee.savoryhubproject.dto.RegisterRequestDTO;
import lk.jiat.ee.savoryhubproject.entity.User;
import lk.jiat.ee.savoryhubproject.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService; // <-- Inject JwtService

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User registerUser(RegisterRequestDTO registerRequestDTO) {
        // Step 1: Check if user with the email already exists
        Optional<User> existingUserOpt = userRepository.findByEmail(registerRequestDTO.getEmail());

        // Check if the Optional contains a user
        if (existingUserOpt.isPresent()) {
            throw new RuntimeException("User with this email already exists.");
        }

        // Step 2: Create a new User entity
        User newUser = new User();
        newUser.setFname(registerRequestDTO.getFname());
        newUser.setLname(registerRequestDTO.getLname());
        newUser.setEmail(registerRequestDTO.getEmail());

        newUser.setContact(registerRequestDTO.getContact());

        // Step 3: Hash the password before saving
        String hashedPassword = passwordEncoder.encode(registerRequestDTO.getPassword());
        newUser.setPassword(hashedPassword);

        newUser.setRole("USER"); // Default role for new users

        // Step 4: Save the new user to the database
        return userRepository.save(newUser);
    }


    // Return an Object array containing both the token and the user
    public Object[] loginUser(LoginRequestDTO loginRequestDTO) {
        // We updated this before to return Optional<User>, so we use orElseThrow
        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with this email."));

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password.");
        }

        // Generate token using the User object itself (since it implements UserDetails)
        String token = jwtService.generateToken(user);

        // Return both the token and the user object in an array
        return new Object[]{token, user};
    }

//    public String loginUser(LoginRequestDTO loginRequestDTO) {
//        User user = userRepository.findByEmail(loginRequestDTO.getEmail());
//        if (user == null) {
//            throw new RuntimeException("User not found with this email.");
//        }
//
//        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
//            throw new RuntimeException("Incorrect password.");
//        }
//
//        // If authentication is successful, generate a JWT
//        // We need to implement UserDetails in our User entity for this. Let's do a quick fix for now.
//        // This will require more changes later in SecurityConfig.
//        var userDetails = org.springframework.security.core.userdetails.User
//                .withUsername(user.getEmail())
//                .password(user.getPassword())
//                // .roles(user.getRole()) // We'll add roles later
//                .build();
//
//        return jwtService.generateToken(userDetails);
//    }

    //{
    //  "fname": "Mashi",
    //  "lname": "Perera",
    //  "email": "mashi.p@example.com",
    //  "password": "password123",
    //  "contact": "0771234567"
    //}
}