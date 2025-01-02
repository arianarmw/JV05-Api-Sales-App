package app.sales.dto.product;

import lombok.Data;

@Data
public class ProductResponse {
    private Integer productId;
    private String productName;
    private String productBrand;
    private Integer productStock;
    private Double productPrice;
    private Integer productDiscount;
    private Double productFinalPrice;

    public ProductResponse() {
    }

    public ProductResponse(Integer productId, String productName, String productBrand, Double productPrice,
            Integer productDiscount, Integer productStock, Double productFinalPrice) {
        this.productId = productId;
        this.productName = productName;
        this.productBrand = productBrand;
        this.productPrice = productPrice;
        this.productDiscount = productDiscount;
        this.productStock = productStock;
        this.productFinalPrice = productFinalPrice;

    }
}
