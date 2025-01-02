package app.sales.dto.login;

import app.sales.entity.Role;
import lombok.Data;

@Data
public class LoginResponse {
    private Integer userId;
    private String username;
    private Role role;
    private String token;
    private String type;
}
