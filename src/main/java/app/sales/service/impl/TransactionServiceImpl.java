package app.sales.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public TransactionResponse createTransaction(Integer productId, int quantity) {
        if (quantity <= 0) {
            throw new RuntimeException("Kuantitas harus lebih besar dari 0.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan."));

        if (product.getDeletedBy() != null || product.getDeletedAt() != null) {
            throw new RuntimeException("Produk ini tidak tersedia.");
        }

        if (product.getProductStock() <= 0) {
            throw new RuntimeException("Produk habis.");
        }

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

        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(user.getUserId());
        userResponse.setFullname(user.getFullName());
        userResponse.setRole(user.getRole().name());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductId(product.getProductId());
        productResponse.setProductName(product.getProductName());
        productResponse.setProductBrand(product.getProductBrand());
        productResponse.setProductStock(product.getProductStock());
        productResponse.setProductPrice(product.getProductPrice());
        productResponse.setProductDiscount(product.getProductDiscount());
        productResponse.setProductFinalPrice(product.getProductFinalPrice());

        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setTransactionId(transaction.getTransactionId());
        transactionResponse.setUser(userResponse);
        transactionResponse.setProduct(productResponse);
        transactionResponse.setQuantity(quantity);
        transactionResponse.setSubtotal(subtotal);
        transactionResponse.setTotal(total);
        transactionResponse.setTransactionDate(transaction.getTransactionDate().toString());

        return transactionResponse;
    }

    @Override
    public Transaction refundTransaction(UUID transactionId, int quantity) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaksi tidak ditemukan."));

        if (quantity > transaction.getQuantity()) {
            throw new RuntimeException("Kuantitas lebih besar dari yang dibeli.");
        }

        Product product = transaction.getProduct();
        product.setProductStock(product.getProductStock() + quantity);
        productRepository.save(product);

        String username = getCurrentUsername();
        transaction.setQuantity(transaction.getQuantity() - quantity);
        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setUpdatedBy(username);

        return transactionRepository.save(transaction);
    }

    @Override
    public List<TransactionResponse> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();

        return transactions.stream().map(transaction -> {
            TransactionResponse response = new TransactionResponse();
            response.setTransactionId(transaction.getTransactionId());

            UserResponse userResponse = new UserResponse();
            userResponse.setUserId(transaction.getUser().getUserId());
            userResponse.setFullname(transaction.getUser().getFullName());
            userResponse.setRole(transaction.getUser().getRole().name());
            response.setUser(userResponse);

            ProductResponse productResponse = new ProductResponse();
            productResponse.setProductId(transaction.getProduct().getProductId());
            productResponse.setProductName(transaction.getProduct().getProductName());
            productResponse.setProductBrand(transaction.getProduct().getProductBrand());
            productResponse.setProductPrice(transaction.getProduct().getProductPrice());
            productResponse.setProductStock(transaction.getProduct().getProductStock());
            productResponse.setProductDiscount(transaction.getProduct().getProductDiscount());
            productResponse.setProductFinalPrice(transaction.getProduct().getProductFinalPrice());
            response.setProduct(productResponse);

            response.setQuantity(transaction.getQuantity());
            response.setSubtotal(transaction.getSubtotal());
            response.setTotal(transaction.getTotal());
            response.setTransactionDate(transaction.getTransactionDate().toString());

            return response;
        }).collect(Collectors.toList());
    }
}
