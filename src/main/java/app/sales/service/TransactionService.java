package app.sales.service;

import java.util.List;
import java.util.UUID;

import app.sales.entity.Transaction;

public interface TransactionService {
    Transaction createTransaction(Integer productId, int quantity);

    Transaction refundTransaction(UUID transactionId, int quantity);

    Transaction getTransactionById(UUID transactionId);

    List<Transaction> getAllTransactions();
}
