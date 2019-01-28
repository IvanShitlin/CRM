import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { Moment } from 'moment';
import * as moment from 'moment';

import { IAgreement } from 'app/shared/model/agreement.model';
import { AgreementService } from 'app/entities/agreement';
import { ITask } from 'app/shared/model/task.model';
import { TaskService } from './task.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-task-update',
    templateUrl: './task-update.component.html'
})
export class TaskUpdateComponent implements OnInit {
    task: ITask;
    isSaving: boolean;

    users: IUser[];
    dateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private taskService: TaskService,
        private agreementService: AgreementService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ task }) => {
            this.task = task;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        if (this.activatedRoute.snapshot.params['agreementId']) {
            this.agreementService
                .find(this.activatedRoute.snapshot.params['agreementId'])
                .subscribe((agreement: HttpResponse<IAgreement>) => {
                    this.task.description = this.formAgreementDescription(agreement.body);
                });
        }
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.task.id !== undefined) {
            this.subscribeToSaveResponse(this.taskService.update(this.task));
        } else {
            this.subscribeToSaveResponse(this.taskService.create(this.task));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITask>>) {
        result.subscribe((res: HttpResponse<ITask>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    private dateFormat(date: Moment) {
        if (!date) {
            return '';
        }
        return moment(date).format('MMM DD, YY');
    }

    private getAgreementViewUrl() {
        const fullUrl = window.location.href;
        return `${fullUrl.slice(0, fullUrl.indexOf('task/new'))}view`;
    }

    private formAgreementDescription(body: IAgreement) {
        if (body === null) {
            return '';
        }
        return (
            `• ${this.getAgreementViewUrl()} \n` +
            `• ${body.clientLastName} ${body.clientFirstName} \n` +
            `• ${body.courseName} \n` +
            `• ${body.status} \n` +
            `• ${this.dateFormat(body.startDate)} - ${this.dateFormat(body.endDate)}`
        );
    }
}
