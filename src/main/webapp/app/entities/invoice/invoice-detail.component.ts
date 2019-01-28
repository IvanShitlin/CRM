import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInvoice } from 'app/shared/model/invoice.model';
import { IPayment } from 'app/shared/model/payment.model';
import { PaymentService } from 'app/entities/payment';

@Component({
    selector: 'jhi-invoice-detail',
    templateUrl: './invoice-detail.component.html'
})
export class InvoiceDetailComponent implements OnInit {
    invoice: IInvoice;
    payment: IPayment;

    constructor(private activatedRoute: ActivatedRoute, private paymentService: PaymentService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ invoice }) => {
            this.invoice = invoice;
        });
        this.paymentService.find(this.invoice.paymentId).subscribe(data => (this.payment = data.body));
    }

    previousState() {
        window.history.back();
    }
}
