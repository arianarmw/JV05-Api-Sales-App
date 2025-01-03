package app.sales.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import app.sales.dto.login.LoginRequest;
import app.sales.dto.register.RegisterRequest;
import app.sales.entity.Role;
import app.sales.entity.User;
import app.sales.repository.UserRepository;
import app.sales.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User login(LoginRequest input) {
        if (input.getUsername() == null || input.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Masukkan username.");
        }
        if (input.getPassword() == null || input.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Masukkan password.");
        }

        Optional<User> userOptional = userRepository.findByUsername(input.getUsername());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Pengguna belum terdaftar.");
        }

        User user = userOptional.get();
        if (!user.getIsActive()) {
            throw new AccountNotActivatedException("Akun belum diaktivasi.");
        }
        if (user.getRole() == Role.KASIR && user.getIsActive()) {
            throw new AccountNotActivatedException("Akun sudah diaktivasi.");
        }

        if (!passwordEncoder.matches(input.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Username dan Password tidak sesuai.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword()));

        return user;
    }

    @Override
    public User register(RegisterRequest input) {
        validateRegisterRequest(input);

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

    private void validateRegisterRequest(RegisterRequest input) {
        if (input.getUsername() == null || input.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username harus diisi.");
        }
        if (!input.getUsername().matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException(
                    "Username harus diisi menggunakan huruf atau kombinasi huruf dan angka.");
        }
        if (userRepository.findByUsername(input.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Akun dengan username tersebut sudah terdaftar.");
        }
        if (input.getFullname() == null || input.getFullname().isEmpty()) {
            throw new IllegalArgumentException("Nama lengkap harus diisi.");
        }
        if (!input.getFullname().matches("^[a-zA-Z'\\s]+$")) {
            throw new IllegalArgumentException(
                    "Nama harus diisi dengan huruf dan atau kombinasi dengan tanda petik \"'\".");
        }
        if (input.getPassword() == null || input.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password harus diisi.");
        }
        if (input.getPassword().length() < 8) {
            throw new IllegalArgumentException(
                    "Password harus diisi minimal 8 karakter dan harus kombinasi huruf, angka, serta karakter apapun.");
        }

        // Password validation rules
        if (input.getPassword().matches("^[a-zA-Z]+$")) {
            throw new IllegalArgumentException(
                    "Password harus kombinasi huruf, angka, serta karakter apapun. Tidak boleh hanya huruf.");
        }
        if (input.getPassword().matches("^\\d+$")) {
            throw new IllegalArgumentException(
                    "Password harus kombinasi huruf, angka, serta karakter apapun. Tidak boleh hanya angka.");
        }
        if (input.getPassword().matches("^[^a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException(
                    "Password harus kombinasi huruf, angka, serta karakter apapun. Tidak boleh hanya karakter spesial.");
        }
        if (input.getPassword().matches("^[a-zA-Z\\d]+$")) {
            throw new IllegalArgumentException(
                    "Password harus kombinasi huruf, angka, serta karakter apapun. Tidak boleh hanya huruf dan angka.");
        }
        if (input.getPassword().matches("^[\\d[^a-zA-Z]]+$")) {
            throw new IllegalArgumentException(
                    "Password harus kombinasi huruf, angka, serta karakter apapun. Tidak boleh hanya angka dan karakter spesial.");
        }
        if (input.getPassword().matches("^[a-zA-Z[^\\d]]+$")) {
            throw new IllegalArgumentException(
                    "Password harus kombinasi huruf, angka, serta karakter apapun. Tidak boleh hanya huruf dan karakter spesial.");
        }
        if (!input.getPassword().equals(input.getConfirmPassword())) {
            throw new IllegalArgumentException("Konfirmasi password tidak sesuai.");
        }
        if (input.getConfirmPassword() == null || input.getConfirmPassword().isEmpty()) {
            throw new IllegalArgumentException("Konfirmasi password harus diisi.");
        }
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
                user.setUpdatedBy(getCurrentUsername());
                userRepository.save(user);
                return "Akun berhasil diaktivasi!";
            } else {
                return "Akun sudah diaktivasi.";
            }
        } else {
            return "Pengguna bukan KASIR.";
        }
    }

    public static class AccountNotActivatedException extends RuntimeException {
        public AccountNotActivatedException(String message) {
            super(message);
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