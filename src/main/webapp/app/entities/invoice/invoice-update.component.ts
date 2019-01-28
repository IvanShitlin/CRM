import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { IInvoice } from 'app/shared/model/invoice.model';
import { InvoiceService } from './invoice.service';
import { IContract } from 'app/shared/model/contract.model';
import { ContractService } from 'app/entities/contract';
import { IPayment } from 'app/shared/model/payment.model';
import { PaymentService } from 'app/entities/payment';
import { Subscription } from 'rxjs/index';

@Component({
    selector: 'jhi-invoice-update',
    templateUrl: './invoice-update.component.html'
})
export class InvoiceUpdateComponent implements OnInit {
    private _invoice: IInvoice;
    isSaving: boolean;

    contracts: IContract[];

    payments: IPayment[];
    dateFromDp: any;
    dateToDp: any;

    eventSubscriber: Subscription;

    constructor(
        private jhiAlertService: JhiAlertService,
        private invoiceService: InvoiceService,
        private contractService: ContractService,
        private paymentService: PaymentService,
        private activatedRoute: ActivatedRoute,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ invoice }) => {
            this.invoice = invoice;
        });
        if (this.invoice.contractId == null) {
            this.invoice.contractId = +this.activatedRoute.snapshot.paramMap.get('contractId');
        }
        this.contractService.find(this.invoice.contractId).subscribe(
            (res: HttpResponse<IContract>) => {
                this.invoice.sum = res.body.price;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        if (this.invoice.id !== undefined) {
            this.registerChangeInInvoice();
        }
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.invoice.id !== undefined) {
            this.subscribeToSaveResponse(this.invoiceService.update(this.invoice));
        } else {
            this.subscribeToSaveResponse(this.invoiceService.create(this.invoice));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IInvoice>>) {
        result.subscribe((res: HttpResponse<IInvoice>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError(res.message));
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError(errorMessage: string) {
        this.isSaving = false;
        this.jhiAlertService.error(errorMessage, null, null);
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackContractById(index: number, item: IContract) {
        return item.id;
    }

    trackPaymentById(index: number, item: IPayment) {
        return item.id;
    }
    get invoice() {
        return this._invoice;
    }

    set invoice(invoice: IInvoice) {
        this._invoice = invoice;
    }

    registerChangeInInvoice() {
        this.eventSubscriber = this.eventManager.subscribe('invoiceAddPayment' + this.invoice.id, response => this.refresh());
    }

    refresh() {
        this.invoiceService.find(this.invoice.id).subscribe(
            (res: HttpResponse<IInvoice>) => {
                this.invoice = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
}
