import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ISalaryItem } from 'app/shared/model/salary-item.model';
import { SalaryItemService } from './salary-item.service';
import { IMentor } from 'app/shared/model/mentor.model';
import { MentorService } from 'app/entities/mentor';
import { IInvoice } from 'app/shared/model/invoice.model';
import { InvoiceService } from 'app/entities/invoice';
import { ISalary } from 'app/shared/model/salary.model';
import { SalaryService } from 'app/entities/salary';
import { Money } from 'app/shared/model/money.model';
import { Currency } from 'app/shared/model/currency.model';

@Component({
    selector: 'jhi-salary-item-update',
    templateUrl: './salary-item-update.component.html'
})
export class SalaryItemUpdateComponent implements OnInit {
    private _salaryItem: ISalaryItem;
    isSaving: boolean;

    mentors: IMentor[];

    invoices: IInvoice[];

    salaries: ISalary[];
    dateFromDp: any;
    dateToDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private salaryItemService: SalaryItemService,
        private mentorService: MentorService,
        private invoiceService: InvoiceService,
        private salaryService: SalaryService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ salaryItem }) => {
            this.salaryItem = salaryItem;
        });
        this.mentorService.query({ filter: 'salaryitem-is-null' }).subscribe(
            (res: HttpResponse<IMentor[]>) => {
                if (!this.salaryItem.mentorId) {
                    this.mentors = res.body;
                } else {
                    this.mentorService.find(this.salaryItem.mentorId).subscribe(
                        (subRes: HttpResponse<IMentor>) => {
                            this.mentors = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.invoiceService.query({ filter: 'salaryitem-is-null' }).subscribe(
            (res: HttpResponse<IInvoice[]>) => {
                if (!this.salaryItem.invoiceId) {
                    this.invoices = res.body;
                } else {
                    this.invoiceService.find(this.salaryItem.invoiceId).subscribe(
                        (subRes: HttpResponse<IInvoice>) => {
                            this.invoices = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.salaryService.query().subscribe(
            (res: HttpResponse<ISalary[]>) => {
                this.salaries = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.salaryItem.id !== undefined) {
            this.subscribeToSaveResponse(this.salaryItemService.update(this.salaryItem));
        } else {
            this.subscribeToSaveResponse(this.salaryItemService.create(this.salaryItem));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISalaryItem>>) {
        result.subscribe((res: HttpResponse<ISalaryItem>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackMentorById(index: number, item: IMentor) {
        return item.id;
    }

    trackInvoiceById(index: number, item: IInvoice) {
        return item.id;
    }

    trackSalaryById(index: number, item: ISalary) {
        return item.id;
    }
    get salaryItem() {
        return this._salaryItem;
    }

    set salaryItem(salaryItem: ISalaryItem) {
        this._salaryItem = salaryItem;
    }
}
