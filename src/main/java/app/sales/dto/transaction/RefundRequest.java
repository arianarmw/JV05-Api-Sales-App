package app.sales.dto.transaction;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class RefundRequest {

    private UUID transactionId; // Menambahkan field transactionId

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
