package app.sales.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import app.sales.dto.product.ProductResponse;
import app.sales.dto.transaction.TransactionResponse;
import app.sales.dto.user.UserResponse;
import app.sales.entity.Product;
import app.sales.entity.Transaction;
import app.sales.entity.User;
import app.sales.repository.ProductRepository;
import app.sales.repository.TransactionRepository;
import app.sales.repository.UserRepository;
import app.sales.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return null;
    }

    @Override
    public Transaction createTransaction(Integer productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan."));

        if (product.getProductStock() < quantity) {
            throw new RuntimeException("Stok tidak mencukupi.");
        }

        product.setProductStock(product.getProductStock() - quantity);
        productRepository.save(product);

        double subtotal = product.getProductPrice() * quantity;
        double total = subtotal;

        String username = getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Pengguna tidak ditemukan"));

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setProduct(product);
        transaction.setQuantity(quantity);
        transaction.setSubtotal(subtotal);
        transaction.setTotal(total);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setCreatedBy(username);
        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setUpdatedBy(username);
        transactionRepository.save(transaction);

        TransactionResponse transactionResponse = new TransactionResponse();
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(user.getUserId());
        userResponse.setFullname(user.getFullName());
        userResponse.setRole(user.getRole().name());
        transactionResponse.setUser(userResponse);

        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductId(product.getProductId());
        productResponse.setProductName(product.getProductName());
        productResponse.setProductBrand(product.getProductBrand());
        productResponse.setProductPrice(product.getProductPrice());
        productResponse.setProductPrice(product.getProductPrice());
        productResponse.setProductDiscount(product.getProductDiscount());
        productResponse.setProductFinalPrice(product.getProductFinalPrice());

        transactionResponse.setProduct(productResponse);
        transactionResponse.setQuantity(quantity);
        transactionResponse.setSubtotal(subtotal);
        transactionResponse.setTotal(total);
        transactionResponse.setTransactionDate(transaction.getTransactionDate().toString());

        return transaction;
    }

    @Override
    public Transaction refundTransaction(UUID transactionId, int quantity) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (quantity > transaction.getQuantity()) {
            throw new RuntimeException("Refund quantity exceeds purchased quantity");
        }

        Product product = transaction.getProduct();
        product.setProductStock(product.getProductStock() + quantity);
        productRepository.save(product);

        transaction.setQuantity(transaction.getQuantity() - quantity);
        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setUpdatedBy("system");
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction getTransactionById(UUID transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
