package app.sales.dto.transaction;

import lombok.Data;

@Data
public class TransactionRequest {
    private Integer productId;
    private int quantity;
}
