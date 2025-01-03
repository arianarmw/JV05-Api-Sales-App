package app.sales.dto.report;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TransactionReport {
    private LocalDate startDate;
    private LocalDate endDate;
}
