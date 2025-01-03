package app.sales.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.sales.dto.ApiResponse;
import app.sales.dto.product.AddProductResponse;
import app.sales.dto.product.ProductResponse;
import app.sales.entity.Product;
import app.sales.repository.CategoryRepository;
import app.sales.repository.ProductRepository;
import app.sales.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<AddProductResponse>> addProduct(@RequestBody Product product) {
        ApiResponse<AddProductResponse> response = new ApiResponse<>();
        try {
            Product savedProduct = productService.addProduct(product);
            AddProductResponse addProductResponse = new AddProductResponse();
            addProductResponse.setProductId(savedProduct.getProductId());
            addProductResponse.setProductName(savedProduct.getProductName());
            addProductResponse.setProductBrand(savedProduct.getProductBrand());
            addProductResponse.setProductPrice(savedProduct.getProductPrice());
            addProductResponse.setProductStock(savedProduct.getProductStock());
            addProductResponse.setProductDiscount(savedProduct.getProductDiscount());
            addProductResponse.setProductFinalPrice(savedProduct.getProductFinalPrice());
            addProductResponse.setCategoryId(savedProduct.getProductCategory().getCategoryId());

            response.setData(addProductResponse);
            response.setMessage("Produk berhasil ditambahkan!");
            response.setStatusCode(HttpStatus.CREATED.value());
            response.setStatus("Sukses");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus("Gagal");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();

        ApiResponse<List<ProductResponse>> response = new ApiResponse<>();
        response.setData(products);
        response.setMessage("Berhasil mengambil data produk.");
        response.setStatusCode(200);
        response.setStatus("Sukses");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Integer productId) {
        try {
            ProductResponse productResponse = productService.getProductById(productId);
            ApiResponse<ProductResponse> response = new ApiResponse<>();
            response.setData(productResponse);
            response.setMessage("Produk berhasil ditemukan.");
            response.setStatusCode(200);
            response.setStatus("Sukses");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<ProductResponse> errorResponse = new ApiResponse<>();
            errorResponse.setData(null);
            errorResponse.setMessage(e.getMessage());
            errorResponse.setStatusCode(404);
            errorResponse.setStatus("Error");

            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @PutMapping("/{productId}/update")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable Integer productId,
            @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProduct(productId, product);
            ProductResponse productResponse = new ProductResponse(
                    updatedProduct.getProductId(),
                    updatedProduct.getProductName(),
                    updatedProduct.getProductBrand(),
                    updatedProduct.getProductPrice(),
                    updatedProduct.getProductDiscount(),
                    updatedProduct.getProductStock(),
                    updatedProduct.getProductFinalPrice());

            ApiResponse<ProductResponse> response = new ApiResponse<>();
            response.setData(productResponse);
            response.setMessage("Data produk berhasil diperbarui!");
            response.setStatusCode(HttpStatus.OK.value());
            response.setStatus("Sukses");

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<ProductResponse> errorResponse = new ApiResponse<>();
            errorResponse.setData(null);
            errorResponse.setMessage(e.getMessage());
            errorResponse.setStatusCode(HttpStatus.CONFLICT.value());
            errorResponse.setStatus("Gagal");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (Exception e) {
            ApiResponse<ProductResponse> errorResponse = new ApiResponse<>();
            errorResponse.setData(null);
            errorResponse.setMessage("Gagal memperbarui data produk.");
            errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            errorResponse.setStatus("Gagal");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/{productId}/delete")
    public ResponseEntity<ApiResponse<ProductResponse>> softDeleteProduct(@PathVariable Integer productId) {
        try {
            Product deletedProduct = productService.softDeleteProduct(productId);
            ProductResponse productResponse = new ProductResponse(
                    deletedProduct.getProductId(),
                    deletedProduct.getProductName(),
                    deletedProduct.getProductBrand(),
                    deletedProduct.getProductPrice(),
                    deletedProduct.getProductDiscount(),
                    deletedProduct.getProductStock(),
                    deletedProduct.getProductFinalPrice());

            ApiResponse<ProductResponse> response = new ApiResponse<>();
            response.setData(productResponse);
            response.setMessage("Produk berhasil dihapus!");
            response.setStatusCode(HttpStatus.OK.value());
            response.setStatus("Sukses");

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<ProductResponse> errorResponse = new ApiResponse<>();
            errorResponse.setData(null);
            errorResponse.setMessage(e.getMessage());
            errorResponse.setStatusCode(HttpStatus.CONFLICT.value());
            errorResponse.setStatus("Gagal");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (Exception e) {
            ApiResponse<ProductResponse> errorResponse = new ApiResponse<>();
            errorResponse.setData(null);
            errorResponse.setMessage("Gagal menghapus produk.");
            errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            errorResponse.setStatus("Gagal");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

}
