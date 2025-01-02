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
import app.sales.repository.ProductRepository;
import app.sales.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<AddProductResponse>> addProduct(@RequestBody Product product) {
        try {
            boolean isProductNameExist = productRepository.existsByProductName(product.getProductName());
            if (isProductNameExist) {
                ApiResponse<AddProductResponse> errorResponse = new ApiResponse<>();
                errorResponse.setData(null);
                errorResponse.setMessage("Produk dengan nama tersebut sudah ada!");
                errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
                errorResponse.setStatus("Gagal");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            Product newProduct = productService.addProduct(product);

            AddProductResponse responseDto = new AddProductResponse();
            responseDto.setProductId(newProduct.getProductId());
            responseDto.setProductName(newProduct.getProductName());
            responseDto.setProductBrand(newProduct.getProductBrand());
            responseDto.setProductPrice(newProduct.getProductPrice());
            responseDto.setProductStock(newProduct.getProductStock());
            responseDto.setProductDiscount(newProduct.getProductDiscount());
            responseDto.setProductFinalPrice(newProduct.getProductFinalPrice());
            responseDto.setCategoryId(newProduct.getProductCategory().getCategoryId());

            ApiResponse<AddProductResponse> apiResponse = new ApiResponse<>();
            apiResponse.setData(responseDto);
            apiResponse.setMessage("Produk berhasil ditambahkan!");
            apiResponse.setStatusCode(HttpStatus.CREATED.value());
            apiResponse.setStatus("Sukses");

            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
        } catch (Exception e) {
            ApiResponse<AddProductResponse> errorResponse = new ApiResponse<>();
            errorResponse.setData(null);
            errorResponse.setMessage("Gagal menambahkan produk");
            errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            errorResponse.setStatus("Gagal");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
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
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Integer productId) {
        try {
            Product product = productService.getProductById(productId);
            ApiResponse<Product> response = new ApiResponse<>();
            response.setData(product);
            response.setMessage("Produk ditemukan.");
            response.setStatusCode(HttpStatus.OK.value());
            response.setStatus("Sukses");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Product> errorResponse = new ApiResponse<>();
            errorResponse.setData(null);
            errorResponse.setMessage("Produk tidak ditemukan.");
            errorResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            errorResponse.setStatus("Gagal");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PutMapping("/{productId}/update")
    public ResponseEntity<ApiResponse<Product>> updateProduct(@PathVariable Integer productId,
            @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProduct(productId, product);
            ApiResponse<Product> response = new ApiResponse<>();
            response.setData(updatedProduct);
            response.setMessage("Data produk berhasil diperbarui!");
            response.setStatusCode(HttpStatus.OK.value());
            response.setStatus("Sukses");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Product> errorResponse = new ApiResponse<>();
            errorResponse.setData(null);
            errorResponse.setMessage("Gagal memperbarui data produk.");
            errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            errorResponse.setStatus("Gagal");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/{productId}/delete")
    public ResponseEntity<ApiResponse<Product>> softDeleteProduct(@PathVariable Integer productId) {
        try {
            Product deletedProduct = productService.softDeleteProduct(productId);
            ApiResponse<Product> response = new ApiResponse<>();
            response.setData(deletedProduct);
            response.setMessage("Produk berhasil dihapus!");
            response.setStatusCode(HttpStatus.OK.value());
            response.setStatus("Sukses");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Product> errorResponse = new ApiResponse<>();
            errorResponse.setData(null);
            errorResponse.setMessage("Gagal menghapus produk.");
            errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            errorResponse.setStatus("Gagal");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
