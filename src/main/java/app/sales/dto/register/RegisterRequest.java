package app.sales.dto.register;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String fullname;
    private String password;
    private String retypePassword;
}
