package app.sales.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import app.sales.dto.auth.LoginRequest;
import app.sales.dto.auth.RegisterRequest;
import app.sales.entity.Role;
import app.sales.entity.User;
import app.sales.repository.UserRepository;
import app.sales.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User login(LoginRequest input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()));

        return userRepository.findByUsername(input.getUsername())
                .orElseThrow();
    }

    @Override
    public User register(RegisterRequest input) {
        if (!input.getPassword().equals(input.getRetypePassword())) {
            throw new IllegalArgumentException("Password dan Retype Password tidak sama");
        }

        if (userRepository.findByUsername(input.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Maaf, Username sudah terdaftar.");
        }

        User user = new User();
        user.setFullName(input.getFullname());
        user.setUsername(input.getUsername());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setRole(Role.KASIR);
        user.setIsActive(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        String currentUsername = getCurrentUsername();
        user.setCreatedBy(currentUsername);
        user.setUpdatedBy(currentUsername);

        return userRepository.save(user);
    }

    public String activateAccount(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            return "Pengguna tidak ditemukan.";
        }

        User user = userOptional.get();

        if (user.getRole() == Role.KASIR) {
            if (!user.getIsActive()) {
                user.setIsActive(true);
                user.setUpdatedAt(LocalDateTime.now());

                String currentUserName = getCurrentUsername();
                user.setUpdatedBy(currentUserName);

                userRepository.save(user);
                return "Akun berhasil diaktivasi!";
            } else {
                return "Akun sudah diaktivasi.";
            }
        } else {
            return "Pengguna bukan KASIR.";
        }
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return null;
    }
}