package app.sales.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.sales.dto.ApiResponse;
import app.sales.dto.product.ProductResponse;
import app.sales.dto.transaction.RefundRequest;
import app.sales.dto.transaction.TransactionRequest;
import app.sales.dto.transaction.TransactionResponse;
import app.sales.dto.user.UserResponse;
import app.sales.entity.Transaction;
import app.sales.service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TransactionResponse>> createTransaction(@RequestBody TransactionRequest request) {
        try {
            TransactionResponse transactionResponse = transactionService.createTransaction(
                    request.getProductId(), request.getQuantity());

            ApiResponse<TransactionResponse> response = new ApiResponse<>();
            response.setData(transactionResponse);
            response.setMessage("Transaksi Berhasil");
            response.setStatusCode(200);
            response.setStatus("Sukses");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<TransactionResponse> errorResponse = new ApiResponse<>();
            errorResponse.setData(null);
            errorResponse.setMessage(e.getMessage());
            errorResponse.setStatusCode(400);
            errorResponse.setStatus("Gagal");

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/refund")
    public ResponseEntity<ApiResponse<TransactionResponse>> refundTransaction(@RequestBody RefundRequest request) {
        ApiResponse<TransactionResponse> apiResponse = new ApiResponse<>();
        try {
            Transaction transaction = transactionService.refundTransaction(request.getTransactionId(),
                    request.getQuantity());
            TransactionResponse responseData = new TransactionResponse();
            responseData.setTransactionId(transaction.getTransactionId());

            UserResponse userResponse = new UserResponse();
            userResponse.setUserId(transaction.getUser().getUserId());
            userResponse.setFullname(transaction.getUser().getFullName());
            userResponse.setRole(transaction.getUser().getRole().name());
            responseData.setUser(userResponse);

            ProductResponse productResponse = new ProductResponse();
            productResponse.setProductId(transaction.getProduct().getProductId());
            productResponse.setProductName(transaction.getProduct().getProductName());
            productResponse.setProductBrand(transaction.getProduct().getProductBrand());
            productResponse.setProductPrice(transaction.getProduct().getProductPrice());
            productResponse.setProductStock(transaction.getProduct().getProductStock());
            productResponse.setProductDiscount(transaction.getProduct().getProductDiscount());
            productResponse.setProductFinalPrice(transaction.getProduct().getProductFinalPrice());
            responseData.setProduct(productResponse);

            responseData.setQuantity(transaction.getQuantity());

            double subtotal = transaction.getProduct().getProductFinalPrice() * transaction.getQuantity();
            responseData.setSubtotal(subtotal);
            responseData.setTotal(subtotal - (subtotal * transaction.getProduct().getProductDiscount() / 100));
            responseData.setTransactionDate(transaction.getCreatedAt().toString());

            double productFinalPrice = transaction.getProduct().getProductFinalPrice();
            double discount = transaction.getProduct().getProductDiscount();
            double refundAmount = (productFinalPrice * request.getQuantity())
                    - (productFinalPrice * request.getQuantity() * discount / 100);
            responseData.setRefundAmount(refundAmount);

            apiResponse.setData(responseData);
            apiResponse.setMessage("Refund berhasil!");
            apiResponse.setStatusCode(200);
            apiResponse.setStatus("Success");

            return ResponseEntity.ok(apiResponse);
        } catch (RuntimeException e) {
            apiResponse.setMessage(e.getMessage());
            apiResponse.setStatusCode(400);
            apiResponse.setStatus("Error");
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getAllTransactions() {
        List<TransactionResponse> transactions = transactionService.getAllTransactions();

        ApiResponse<List<TransactionResponse>> response = new ApiResponse<>();
        response.setData(transactions);
        response.setMessage("Berhasil mengambil daftar transaksi!");
        response.setStatusCode(200);
        response.setStatus("Sukses");

        return ResponseEntity.ok(response);
    }
}
