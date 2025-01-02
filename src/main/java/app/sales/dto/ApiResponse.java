package app.sales.dto;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private T data;
    private String message;
    private int statusCode;
    private String status;
}
