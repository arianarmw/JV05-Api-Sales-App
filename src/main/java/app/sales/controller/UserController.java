package app.sales.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.sales.dto.ApiResponse;
import app.sales.dto.login.LoginRequest;
import app.sales.dto.login.LoginResponse;
import app.sales.dto.register.RegisterRequest;
import app.sales.entity.User;
import app.sales.repository.UserRepository;
import app.sales.service.JwtService;
import app.sales.service.UserService;
import app.sales.service.impl.UserServiceImpl.AccountNotActivatedException;

@RequestMapping("/users")
@RestController
public class UserController {
    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    private UserRepository userRepository;

    public UserController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        try {
            User authenticatedUser = userService.login(loginRequest);

            String jwtToken = jwtService.generateToken(authenticatedUser);

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setUserId(authenticatedUser.getUserId());
            loginResponse.setUsername(authenticatedUser.getUsername());
            loginResponse.setRole(authenticatedUser.getRole());
            loginResponse.setToken(jwtToken);
            loginResponse.setType("Bearer");

            ApiResponse<LoginResponse> apiResponse = new ApiResponse<>();
            apiResponse.setData(loginResponse);
            apiResponse.setMessage("Login Berhasil!");
            apiResponse.setStatusCode(HttpStatus.OK.value());
            apiResponse.setStatus("Sukses");

            return ResponseEntity.ok(apiResponse);
        } catch (AccountNotActivatedException e) {
            ApiResponse<LoginResponse> errorResponse = new ApiResponse<>();
            errorResponse.setData(null);
            errorResponse.setMessage("Login gagal: " + "Akun belum diaktivasi.");
            errorResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            errorResponse.setStatus("Gagal");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception e) {
            ApiResponse<LoginResponse> errorResponse = new ApiResponse<>();
            errorResponse.setData(null);
            errorResponse.setMessage("Login gagal: " + "Username atau Password tidak sesuai.");
            errorResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            errorResponse.setStatus("Gagal");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> register(@RequestBody RegisterRequest registerRequest) {
        try {
            User newUser = userService.register(registerRequest);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("Username", newUser.getUsername());
            responseData.put("isActive", newUser.getIsActive());

            ApiResponse<Object> apiResponse = new ApiResponse<>();
            apiResponse.setData(responseData);
            apiResponse.setMessage("User " + newUser.getUsername() + " berhasil terdaftar!");
            apiResponse.setStatusCode(HttpStatus.CREATED.value());
            apiResponse.setStatus("Sukses");

            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);

        } catch (Exception e) {
            ApiResponse<Object> errorResponse = new ApiResponse<>();
            errorResponse.setData(null);
            errorResponse.setMessage("Pendaftaran akun gagal: " + e.getMessage());
            errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            errorResponse.setStatus("Gagal");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/activate/{username}")
    public ResponseEntity<ApiResponse<String>> activateAccount(@PathVariable String username) {
        String result = userService.activateAccount(username);

        ApiResponse<String> apiResponse = new ApiResponse<>();

        if (result.equals("Pengguna tidak ditemukan")) {
            apiResponse.setMessage(result);
            apiResponse.setStatusCode(400);
            apiResponse.setStatus("Gagal");
            apiResponse.setData(null);
            return ResponseEntity.status(400).body(apiResponse);
        }

        if (result.equals("Akun sudah aktif atau pengguna bukan KASIR")) {
            apiResponse.setMessage(result);
            apiResponse.setStatusCode(400);
            apiResponse.setStatus("Gagal");
            apiResponse.setData(null);
            return ResponseEntity.status(400).body(apiResponse);
        }

        apiResponse.setMessage("Akun berhasil diaktivasi");
        apiResponse.setStatusCode(200);
        apiResponse.setStatus("Sukses");
        apiResponse.setData(username);

        return ResponseEntity.ok(apiResponse);
    }

}
