package app.sales.dto.transaction;

import java.util.UUID;

import app.sales.dto.product.ProductResponse;
import app.sales.dto.user.UserResponse;
import lombok.Data;

@Data
public class TransactionResponse {
    private UUID transactionId;
    private UserResponse user;
    private ProductResponse product;
    private int quantity;
    private double subtotal;
    private double total;
    private String transactionDate;
    private double refundAmount;
}
