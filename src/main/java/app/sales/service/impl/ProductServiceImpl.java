package app.sales.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import app.sales.dto.product.ProductResponse;
import app.sales.entity.Product;
import app.sales.repository.CategoryRepository;
import app.sales.repository.ProductRepository;
import app.sales.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Product addProduct(Product product) {
        double finalPrice = product.getProductPrice();
        if (product.getProductDiscount() != null && product.getProductDiscount() > 0) {
            finalPrice = finalPrice - (finalPrice * product.getProductDiscount() / 100);
        }
        product.setProductFinalPrice(finalPrice);

        String currentUsername = getCurrentUsername();
        product.setCreatedBy(currentUsername);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedBy(currentUsername);
        product.setUpdatedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);

        return savedProduct;
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return null;
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> new ProductResponse(
                        product.getProductId(),
                        product.getProductName(),
                        product.getProductBrand(),
                        Double.valueOf(product.getProductPrice()),
                        product.getProductDiscount(),
                        product.getProductStock(),
                        Double.valueOf(product.getProductFinalPrice())))
                .collect(Collectors.toList());
    }

    @Override
    public Product getProductById(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produk dengan id " + productId + "tidak ditemukan."));
    }

    @Override
    public Product updateProduct(Integer productId, Product product) {
        Product existingProduct = getProductById(productId);

        existingProduct.setProductName(product.getProductName());
        existingProduct.setProductBrand(product.getProductBrand());
        existingProduct.setProductStock(product.getProductStock());
        existingProduct.setProductPrice(product.getProductPrice());
        existingProduct.setProductDiscount(product.getProductDiscount());

        double finalPrice = product.getProductPrice();
        if (product.getProductDiscount() != null && product.getProductDiscount() > 0) {
            finalPrice = finalPrice - (finalPrice * product.getProductDiscount() / 100);
        }

        existingProduct.setProductFinalPrice(finalPrice);
        existingProduct.setProductCategory(product.getProductCategory());
        existingProduct.setUpdatedAt(LocalDateTime.now());
        existingProduct.setUpdatedBy(getCurrentUsername());

        return productRepository.save(existingProduct);
    }

    @Override
    public Product softDeleteProduct(Integer productId) {
        Product existingProduct = getProductById(productId);

        existingProduct.setDeletedAt(LocalDateTime.now());
        existingProduct.setDeletedBy(getCurrentUsername());

        return productRepository.save(existingProduct);
    }

}
