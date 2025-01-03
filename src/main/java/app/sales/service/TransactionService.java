package app.sales.service;

import java.util.List;
import java.util.UUID;

import app.sales.dto.transaction.TransactionResponse;
import app.sales.entity.Transaction;

public interface TransactionService {
    TransactionResponse createTransaction(Integer productId, int quantity);

    Transaction refundTransaction(UUID transactionId, int quantity);

    List<TransactionResponse> getAllTransactions();
}
