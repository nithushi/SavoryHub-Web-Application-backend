package lk.jiat.ee.savoryhubproject.service;

import lk.jiat.ee.savoryhubproject.dto.ChangePasswordDTO;
import lk.jiat.ee.savoryhubproject.dto.UpdateUserDTO;
import lk.jiat.ee.savoryhubproject.entity.User;
import lk.jiat.ee.savoryhubproject.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Path imageUploadDir;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.imageUploadDir = Paths.get("uploads/profile-images").toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.imageUploadDir); // 2. Directory එක හදනවා (නැත්නම්)
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }

    // --- ADD THIS NEW METHOD ---
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @Transactional
    public User updateUserDetails(String email, UpdateUserDTO updateUserDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFname(updateUserDTO.getFname());
        user.setLname(updateUserDTO.getLname());
        user.setContact(updateUserDTO.getContact());

        return userRepository.save(user);
    }

    // --- NEW METHOD to CHANGE PASSWORD ---
    @Transactional
    public void changePassword(String email, ChangePasswordDTO changePasswordDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Check if the provided current password is correct
        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect current password.");
        }

        // 4. If correct, encode the new password and save it
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
    }

    // --- NEW METHOD to UPLOAD PROFILE IMAGE ---
    @Transactional
    public String uploadProfileImage(String email, MultipartFile file) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));

        // 1. Validate file (optional but recommended)
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }
        if (!file.getContentType().startsWith("image/")) {
            throw new RuntimeException("Only image files are allowed.");
        }

        try {
            // 2. File එකට unique නමක් දෙනවා
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path targetLocation = this.imageUploadDir.resolve(fileName);

            // 3. File එක save කරනවා
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // 4. Old image එක delete කරනවා (optional)
            if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()) {
                Path oldImagePath = Paths.get(user.getProfileImage()); // මේක `uploads/profile-images/` වලින් පටන් ගත යුතුයි
                if (Files.exists(oldImagePath)) {
                    Files.delete(oldImagePath);
                }
            }

            // 5. User ගේ profileImage field එක update කරනවා
            String imagePathForDb = "/uploads/profile-images/" + fileName; // Frontend එකට පෙන්වන්න පුළුවන් විදිහට Path එක හදනවා
            user.setProfileImage(imagePathForDb);
            userRepository.save(user);

            return imagePathForDb; // Frontend එකට අලුත් image path එක යවනවා
        } catch (IOException ex) {
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), ex);
        }
    }

    // --- NEW METHOD for ADMIN: Get all users ---
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // --- NEW METHOD for ADMIN: Update user role ---
    @Transactional
    public User updateUserRole(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // You can add validation here to ensure role is either 'USER' or 'ADMIN'
        user.setRole(role);
        return userRepository.save(user);
    }

    // --- NEW METHOD to TOGGLE USER STATUS ---
    @Transactional
    public User toggleUserStatus(Long userId, String currentAdminEmail) {
        User userToToggle = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Prevent an admin from deactivating their own account
        if (userToToggle.getEmail().equals(currentAdminEmail)) {
            throw new RuntimeException("Admin cannot deactivate their own account.");
        }

        // Toggle the status
        userToToggle.setEnabled(!userToToggle.isEnabled());
        return userRepository.save(userToToggle);
    }
}