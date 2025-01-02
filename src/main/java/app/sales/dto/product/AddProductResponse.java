package app.sales.dto.product;

import lombok.Data;

@Data
public class AddProductResponse {
    private Integer productId;
    private String productName;
    private Double productPrice;
    private Integer productStock;
    private Integer productDiscount;
    private Double productFinalPrice;
    private Integer categoryId;
}