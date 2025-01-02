package app.sales.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.sales.dto.ApiResponse;
import app.sales.dto.transaction.RefundRequest;
import app.sales.dto.transaction.TransactionRequest;
import app.sales.entity.Transaction;
import app.sales.service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Transaction>> createTransaction(@RequestBody TransactionRequest request) {
        Transaction transaction = transactionService.createTransaction(request.getProductId(), request.getQuantity());

        ApiResponse<Transaction> response = new ApiResponse<>();
        response.setData(transaction);
        response.setMessage("Transaksi Berhasil");
        response.setStatusCode(200);
        response.setStatus("Sukses");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refund")
    public ResponseEntity<ApiResponse<Transaction>> refundTransaction(@RequestBody RefundRequest request) {
        Transaction transaction = transactionService.refundTransaction(request.getTransactionId(),
                request.getQuantity());

        ApiResponse<Transaction> response = new ApiResponse<>();
        response.setData(transaction);
        response.setMessage("Refund berhasil!");
        response.setStatusCode(200);
        response.setStatus("Success");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<ApiResponse<Transaction>> getTransactionById(@PathVariable UUID transactionId) {
        Transaction transaction = transactionService.getTransactionById(transactionId);

        ApiResponse<Transaction> response = new ApiResponse<>();
        response.setData(transaction);
        response.setMessage("Transaction fetched successfully");
        response.setStatusCode(200);
        response.setStatus("Success");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<Transaction>>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        ApiResponse<List<Transaction>> response = new ApiResponse<>();
        response.setData(transactions);
        response.setMessage("Transactions fetched successfully");
        response.setStatusCode(200);
        response.setStatus("Success");

        return ResponseEntity.ok(response);
    }
}
