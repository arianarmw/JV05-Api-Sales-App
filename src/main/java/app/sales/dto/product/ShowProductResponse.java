package app.sales.dto.product;

import lombok.Data;

@Data
public class ShowProductResponse {
    private Integer productId;
    private String productName;
    private Integer productStock;
    private Double productFinalPrice;

    public ShowProductResponse(Integer productId, String productName, Integer productStock, Double productFinalPrice) {
        this.productId = productId;
        this.productName = productName;
        this.productStock = productStock;
        this.productFinalPrice = productFinalPrice;

    }
}
