package app.sales.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.sales.entity.Product;
import app.sales.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addProduct(@RequestBody Product product) {
        try {
            Product newProduct = productService.addProduct(product);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("productId", newProduct.getProductId());
            responseData.put("productName", newProduct.getProductName());
            responseData.put("productPrice", newProduct.getProductPrice());
            responseData.put("productStock", newProduct.getProductStock());
            responseData.put("productDiscount", newProduct.getProductDiscount());
            responseData.put("productFinalPrice", newProduct.getProductFinalPrice());

            Map<String, Object> response = new HashMap<>();
            response.put("data", responseData);
            response.put("message", "Produk berhasil ditambahkan!");
            response.put("statusCode", HttpStatus.CREATED.value());
            response.put("status", "Sukses");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Product addition failed: " + e.getMessage());
            errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("status", "failure");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
