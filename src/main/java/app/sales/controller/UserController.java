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
import app.sales.service.JwtService;
import app.sales.service.UserService;
import app.sales.service.impl.UserServiceImpl.AccountNotActivatedException;

@RequestMapping("/users")
@RestController
public class UserController {
    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    public UserController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        ApiResponse<LoginResponse> response = new ApiResponse<>();

        try {
            User authenticatedUser = userService.login(loginRequest);

            String jwtToken = jwtService.generateToken(authenticatedUser);

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setUserId(authenticatedUser.getUserId());
            loginResponse.setUsername(authenticatedUser.getUsername());
            loginResponse.setRole(authenticatedUser.getRole());
            loginResponse.setToken(jwtToken);
            loginResponse.setType("Bearer");

            response.setData(loginResponse);
            response.setMessage("Login Berhasil!");
            response.setStatusCode(HttpStatus.OK.value());
            response.setStatus("Sukses");

            return ResponseEntity.ok(response);

        } catch (AccountNotActivatedException e) {
            response.setData(null);
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            response.setStatus("Gagal");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        } catch (IllegalArgumentException e) {
            response.setData(null);
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus("Gagal");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            response.setData(null);
            response.setMessage("Terjadi kesalahan pada sistem.");
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setStatus("Gagal");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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

        } catch (IllegalArgumentException e) {
            ApiResponse<Object> errorResponse = new ApiResponse<>();
            errorResponse.setData(null);
            errorResponse.setMessage(e.getMessage());
            errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            errorResponse.setStatus("Gagal");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/activate/{username}")
    public ResponseEntity<ApiResponse<String>> activateAccount(@PathVariable String username) {
        String result = userService.activateAccount(username);

        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(result);

        if (result.equals("Pengguna tidak ditemukan") || result.equals("Akun sudah diaktivasi.")) {
            apiResponse.setStatusCode(400);
            apiResponse.setStatus("Gagal");
        } else {
            apiResponse.setStatusCode(200);
            apiResponse.setStatus("Sukses");
        }

        apiResponse.setData(result.equals("Akun berhasil diaktivasi!") ? username : null);
        return ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse);
    }
}
