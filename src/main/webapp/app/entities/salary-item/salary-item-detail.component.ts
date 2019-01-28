import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISalaryItem } from 'app/shared/model/salary-item.model';

@Component({
    selector: 'jhi-salary-item-detail',
    templateUrl: './salary-item-detail.component.html'
})
export class SalaryItemDetailComponent implements OnInit {
    salaryItem: ISalaryItem;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ salaryItem }) => {
            this.salaryItem = salaryItem;
        });
    }

    previousState() {
        window.history.back();
    }
}
