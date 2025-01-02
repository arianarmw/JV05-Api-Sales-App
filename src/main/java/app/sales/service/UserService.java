package app.sales.service;

import app.sales.dto.login.LoginRequest;
import app.sales.dto.register.RegisterRequest;
import app.sales.entity.User;

public interface UserService {
    User login(LoginRequest input);

    User register(RegisterRequest input);

    String activateAccount(String username);

}