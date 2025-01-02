package app.sales.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.sales.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByProductName(String productName);
}
