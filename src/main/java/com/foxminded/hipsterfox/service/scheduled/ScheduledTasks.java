package com.foxminded.hipsterfox.service.scheduled;

import com.foxminded.hipsterfox.domain.Contract;
import com.foxminded.hipsterfox.domain.Invoice;
import com.foxminded.hipsterfox.repository.ContractRepository;
import com.foxminded.hipsterfox.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ScheduledTasks {
    private final ContractRepository contractRepository;
    private final InvoiceRepository invoiceRepository;

    @Value("${application.schedule.invoice.issueDaysBeforePaymentDate}")
    private int issueDaysBeforePaymentDate;

    public ScheduledTasks(ContractRepository contractRepository, InvoiceRepository invoiceRepository) {
        this.contractRepository = contractRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Scheduled(cron = "${application.schedule.invoice.creationCron}")
    public void createInvoices() {
        LocalDate invoiceCreationDate = LocalDate.now().plusDays(issueDaysBeforePaymentDate);

        List<Contract> contracts = contractRepository.findAllByNextPayDate(invoiceCreationDate);

        for (Contract contract : contracts) {
            if (!invoiceRepository.existsByDateFromAndContract(invoiceCreationDate, contract)) {
                Invoice invoice = new Invoice();
                invoice.setContract(contract);
                invoice.setDateFrom(invoiceCreationDate);
                invoice.setDateTo(invoiceCreationDate.plusMonths(1).minusDays(1));
                invoice.setSum(contract.getPrice());
                invoiceRepository.save(invoice);
            }
        }
    }
}
