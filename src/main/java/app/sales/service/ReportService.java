package app.sales.service;

import java.util.List;

import app.sales.dto.ApiResponse;
import app.sales.dto.report.TransactionReport;
import app.sales.entity.Transaction;

public interface ReportService {
    ApiResponse<List<Transaction>> getTransactionsByDateRange(TransactionReport filterRequest);
}
