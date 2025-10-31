package lk.jiat.ee.savoryhubproject.controller;

import lk.jiat.ee.savoryhubproject.dto.ChangePasswordDTO;
import lk.jiat.ee.savoryhubproject.dto.UpdateUserDTO;
import lk.jiat.ee.savoryhubproject.entity.User;
import lk.jiat.ee.savoryhubproject.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    // You only need UserService here now
    private final UserService userService;

    // --- CORRECTED CONSTRUCTOR ---
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint to get details of the currently logged-in user
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUserDetails(Authentication authentication) {
        String email = authentication.getName();
        // It's better to get the user through the service
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUserDetails(@RequestBody UpdateUserDTO updateUserDTO, Authentication authentication) {
        String email = authentication.getName();
        User updatedUser = userService.updateUserDetails(email, updateUserDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO, Authentication authentication) {
        try {
            userService.changePassword(authentication.getName(), changePasswordDTO);
            return ResponseEntity.ok("Password changed successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProfileImage(@RequestParam("image") MultipartFile image, Authentication authentication) {
        try {
            String imageUrl = userService.uploadProfileImage(authentication.getName(), image);
            return ResponseEntity.ok(imageUrl); // Success message or URL
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- ADMIN ONLY ENDPOINTS ---

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUserRole(@PathVariable Long userId, @RequestBody Map<String, String> roleUpdate) {
        String role = roleUpdate.get("role");
        User updatedUser = userService.updateUserRole(userId, role);
        return ResponseEntity.ok(updatedUser);
    }

    // --- NEW ENDPOINT to ADMIN TOGGLE USER STATUS ---
    @PutMapping("/{userId}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> toggleUserStatus(@PathVariable Long userId, Authentication authentication) {
        try {
            String currentAdminEmail = authentication.getName();
            User updatedUser = userService.toggleUserStatus(userId, currentAdminEmail);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
