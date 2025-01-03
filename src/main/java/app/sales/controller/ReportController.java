package app.sales.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.sales.dto.ApiResponse;
import app.sales.dto.report.TransactionReport;
import app.sales.entity.Transaction;
import app.sales.service.ReportService;

@RestController
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/transactions")
    public ApiResponse<List<Transaction>> getTransactionsByDate(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {

        TransactionReport filterRequest = new TransactionReport();
        filterRequest.setStartDate(LocalDate.parse(startDate));
        filterRequest.setEndDate(LocalDate.parse(endDate));

        return reportService.getTransactionsByDateRange(filterRequest);
    }
}
