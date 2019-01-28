import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IAgreement } from 'app/shared/model/agreement.model';
import { AgreementService } from './agreement.service';
import { IClient } from 'app/shared/model/client.model';
import { ClientService } from 'app/entities/client';
import { ICourse } from 'app/shared/model/course.model';
import { CourseService } from 'app/entities/course';
import { ICourseType } from 'app/shared/model/course-type.model';
import { CourseTypeService } from 'app/entities/course-type';

@Component({
    selector: 'jhi-agreement-update',
    templateUrl: './agreement-update.component.html'
})
export class AgreementUpdateComponent implements OnInit {
    private _agreement: IAgreement;
    isSaving: boolean;
    clients: IClient[];
    courseTypes: ICourseType[];

    courses: ICourse[];
    startDateDp: any;
    endDateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private agreementService: AgreementService,
        private clientService: ClientService,
        private courseTypeService: CourseTypeService,
        private courseService: CourseService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;

        this.activatedRoute.data.subscribe(({ agreement }) => {
            this.agreement = agreement;
        });

        this.clientService.query({ filter: 'agreement-is-null' }).subscribe(
            (res: HttpResponse<IClient[]>) => {
                if (!this.agreement.clientId) {
                    this.clients = res.body;
                } else {
                    this.clientService.find(this.agreement.clientId).subscribe(
                        (subRes: HttpResponse<IClient>) => {
                            this.clients = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );

        if (this.activatedRoute.snapshot.params['clientId']) {
            this.clientService.find(this.activatedRoute.snapshot.params['clientId']).subscribe((client: HttpResponse<IClient>) => {
                this.agreement.clientId = client.body.id;
                this.agreement.clientFirstName = client.body.firstName;
                this.agreement.clientLastName = client.body.lastName;
            });
        }

        if (this.activatedRoute.snapshot.params['courseType.id']) {
            this.courseTypeService
                .find(this.activatedRoute.snapshot.params['courseType.id'])
                .subscribe((courseType: HttpResponse<ICourseType>) => {
                    this.agreement.courseType = courseType.body;
                });
        }

        this.courseTypeService.query({ filter: 'agreement-is-null' }).subscribe(
            (res: HttpResponse<ICourseType[]>) => {
                this.courseTypes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );

        this.courseService.query({ filter: 'agreement-is-null' }).subscribe(
            (res: HttpResponse<ICourse[]>) => {
                this.courses = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.agreement.id !== undefined) {
            this.subscribeToSaveResponse(this.agreementService.update(this.agreement));
        } else {
            this.subscribeToSaveResponse(this.agreementService.create(this.agreement));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IAgreement>>) {
        result.subscribe((res: HttpResponse<IAgreement>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackClientById(index: number, item: IClient) {
        return item.id;
    }

    trackCourseById(index: number, item: ICourse) {
        return item.id;
    }

    trackCourseTypeById(index: number, item: ICourseType) {
        return item.id;
    }

    get agreement() {
        return this._agreement;
    }

    set agreement(agreement: IAgreement) {
        this._agreement = agreement;
    }
}
