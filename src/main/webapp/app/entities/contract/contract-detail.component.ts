import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContract } from 'app/shared/model/contract.model';
import { IInvoice } from 'app/shared/model/invoice.model';
import { InvoiceService } from 'app/entities/invoice';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { JhiAlertService } from 'ng-jhipster';

@Component({
    selector: 'jhi-contract-detail',
    templateUrl: './contract-detail.component.html'
})
export class ContractDetailComponent implements OnInit {
    contract: IContract;
    invoices: IInvoice[];

    constructor(private activatedRoute: ActivatedRoute, private invoiceService: InvoiceService, private jhiAlertService: JhiAlertService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ contract }) => {
            this.contract = contract;
        });
        const criteria = [{ key: 'contractId.equals', value: this.contract.id }];
        this.invoiceService
            .query({
                criteria
            })
            .subscribe(
                (res: HttpResponse<IInvoice[]>) => this.paginateInvoices(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    private paginateInvoices(data: IInvoice[], headers: HttpHeaders) {
        this.invoices = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    previousState() {
        window.history.back();
    }
}
