package app.sales.dto.user;

import lombok.Data;

@Data
public class UserResponse {
    private Integer userId;
    private String fullname;
    private String role;
}
