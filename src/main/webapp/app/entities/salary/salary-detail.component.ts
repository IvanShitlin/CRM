import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISalary } from 'app/shared/model/salary.model';
import { ISalaryItem } from 'app/shared/model/salary-item.model';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { SalaryItemService } from 'app/entities/salary-item';
import { JhiAlertService } from 'ng-jhipster';

@Component({
    selector: 'jhi-salary-detail',
    templateUrl: './salary-detail.component.html'
})
export class SalaryDetailComponent implements OnInit {
    salary: ISalary;
    includedItems: ISalaryItem[];

    constructor(
        private activatedRoute: ActivatedRoute,
        private salaryItemService: SalaryItemService,
        private jhiAlertService: JhiAlertService
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ salary }) => {
            this.salary = salary;
        });
        const criteria = [{ key: 'salaryId.equals', value: this.salary.id }];
        this.salaryItemService.query({ criteria }).subscribe(
            (res: HttpResponse<ISalaryItem[]>) => {
                this.includedItems = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    previousState() {
        window.history.back();
    }
}
