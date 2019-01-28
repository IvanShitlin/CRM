import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ISalary } from 'app/shared/model/salary.model';
import { SalaryService } from './salary.service';
import { IMentor } from 'app/shared/model/mentor.model';
import { MentorService } from 'app/entities/mentor';
import { ISalaryItem } from 'app/shared/model/salary-item.model';
import { SalaryItemService } from 'app/entities/salary-item';
import { Money } from 'app/shared/model/money.model';
import { Currency } from 'app/shared/model/currency.model';

@Component({
    selector: 'jhi-salary-update',
    templateUrl: './salary-update.component.html'
})
export class SalaryUpdateComponent implements OnInit {
    private _salary: ISalary;
    isSaving: boolean;

    mentors: IMentor[];
    notAccountedItems: ISalaryItem[];
    selectedItems: ISalaryItem[];

    dateFromDp: any;
    dateToDp: any;
    paidDateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private salaryService: SalaryService,
        private mentorService: MentorService,
        private salaryItemService: SalaryItemService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ salary }) => {
            this.salary = salary;
        });
        this.mentorService.query({ filter: 'salary-is-null' }).subscribe(
            (res: HttpResponse<IMentor[]>) => {
                if (!this.salary.mentorId) {
                    this.mentors = res.body;
                } else {
                    this.mentorService.find(this.salary.mentorId).subscribe(
                        (subRes: HttpResponse<IMentor>) => {
                            this.mentors = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        if (this.salary.id === undefined) {
            this.salary.sum = new Money(0, Currency.UAH);
        }
        this.initNotAccountedItems();
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.salary.id !== undefined) {
            this.subscribeToSaveResponse(this.salaryService.update(this.salary));
        } else {
            this.subscribeToSaveResponse(this.salaryService.create(this.salary));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISalary>>) {
        result.subscribe((res: HttpResponse<ISalary>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    private initNotAccountedItems() {
        const criteria = [{ key: 'accounted.equals', value: false }, { key: 'mentorId.equals', value: this._salary.mentorId }];
        this.salaryItemService.query({ criteria }).subscribe(
            (res: HttpResponse<ISalaryItem[]>) => {
                this.notAccountedItems = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    trackMentorById(index: number, item: IMentor) {
        return item.id;
    }

    trackSalaryById(index: number, item: ISalary) {
        return item.id;
    }

    get salary() {
        return this._salary;
    }

    set salary(salary: ISalary) {
        this._salary = salary;
    }

    removeItem(entity) {
        this.salary.items = this.salary.items.filter(item => item !== entity);
        this.notAccountedItems.push(entity);
        this.salary.sum.amount = this.salary.sum.amount - entity.sum.amount;
    }

    addItems() {
        this.salary.items = this.salary.items.concat(this.selectedItems);
        for (const entity of this.selectedItems) {
            this.notAccountedItems = this.notAccountedItems.filter(item => item !== entity);
            this.salary.sum.amount = this.salary.sum.amount + entity.sum.amount;
        }
    }
}
