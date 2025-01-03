package app.sales.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.sales.dto.ApiResponse;
import app.sales.dto.report.TransactionReport;
import app.sales.entity.Transaction;
import app.sales.repository.ReportRepository;
import app.sales.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportRepository reportRepository;

    public ApiResponse<List<Transaction>> getTransactionsByDateRange(TransactionReport filterRequest) {
        LocalDateTime startDate = filterRequest.getStartDate().atStartOfDay();
        LocalDateTime endDate = filterRequest.getEndDate().atTime(23, 59, 59);
        List<Transaction> transactions = reportRepository.findTransactionsBetweenDates(startDate, endDate);

        ApiResponse<List<Transaction>> response = new ApiResponse<>();
        response.setData(transactions);
        response.setMessage("Berhasil mengambil laporan transaksi.");
        response.setStatusCode(200);
        response.setStatus("Success");

        return response;
    }
}
