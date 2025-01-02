package app.sales.service;

import java.util.List;

import app.sales.dto.product.ShowProductResponse;
import app.sales.entity.Product;

public interface ProductService {
    Product addProduct(Product product);

    List<ShowProductResponse> getAllProducts();

    Product getProductById(Integer productId);

    Product updateProduct(Integer productId, Product product);

    Product softDeleteProduct(Integer productId);
}
