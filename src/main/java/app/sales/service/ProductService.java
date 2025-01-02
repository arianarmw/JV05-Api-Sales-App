package app.sales.service;

import java.util.List;

import app.sales.entity.Product;

public interface ProductService {
    Product addProduct(Product product);

    List<Product> getAllProducts();
}
