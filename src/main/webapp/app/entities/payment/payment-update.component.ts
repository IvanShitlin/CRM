import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IPayment } from 'app/shared/model/payment.model';
import { PaymentService } from './payment.service';
import { IInvoice } from 'app/shared/model/invoice.model';
import { InvoiceService } from 'app/entities/invoice';
import { Money } from 'app/shared/model/money.model';
import { Currency } from 'app/shared/model/currency.model';

@Component({
    selector: 'jhi-payment-update',
    templateUrl: './payment-update.component.html'
})
export class PaymentUpdateComponent implements OnInit {
    private _payment: IPayment;
    isSaving: boolean;

    invoices: IInvoice[];
    dateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private paymentService: PaymentService,
        private invoiceService: InvoiceService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ payment }) => {
            this.payment = payment;
        });
        this.invoiceService.query({ filter: 'payment-is-null' }).subscribe(
            (res: HttpResponse<IInvoice[]>) => {
                if (!this.payment.invoiceId) {
                    this.invoices = res.body;
                } else {
                    this.invoiceService.find(this.payment.invoiceId).subscribe(
                        (subRes: HttpResponse<IInvoice>) => {
                            this.invoices = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.payment.id !== undefined) {
            this.subscribeToSaveResponse(this.paymentService.update(this.payment));
        } else {
            this.subscribeToSaveResponse(this.paymentService.create(this.payment));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>) {
        result.subscribe((res: HttpResponse<IPayment>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackInvoiceById(index: number, item: IInvoice) {
        return item.id;
    }
    get payment() {
        return this._payment;
    }

    set payment(payment: IPayment) {
        this._payment = payment;
    }
}
