package app.sales.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import app.sales.entity.Category;
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
        Category category = categoryRepository.findById(product.getProductCategory().getCategoryId())
                .orElseThrow(() -> new RuntimeException("Kategori tidak ditemukan"));
        product.setProductCategory(category);

        double finalPrice = product.getProductPrice();
        if (product.getProductDiscount() != null && product.getProductDiscount() > 0) {
            finalPrice = finalPrice - (finalPrice * product.getProductDiscount() / 100);
        }
        product.setProductFinalPrice(finalPrice);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserRole = authentication.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .orElse("UNKNOWN_ROLE");

        product.setCreatedBy(currentUserRole);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedBy(currentUserRole);
        product.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
