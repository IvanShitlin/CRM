import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IContract } from 'app/shared/model/contract.model';
import { ContractService } from './contract.service';
import { IMentor } from 'app/shared/model/mentor.model';
import { MentorService } from 'app/entities/mentor';
import { IAgreement } from 'app/shared/model/agreement.model';
import { AgreementService } from 'app/entities/agreement';
import { CourseService } from 'app/entities/course';
import { ICourseType } from 'app/shared/model/course-type.model';
import { CourseTypeService } from 'app/entities/course-type';

@Component({
    selector: 'jhi-contract-update',
    templateUrl: './contract-update.component.html'
})
export class ContractUpdateComponent implements OnInit {
    private _contract: IContract;
    isSaving: boolean;

    mentors: IMentor[];
    agreements: IAgreement[];
    courseType: string;
    startDateDp: any;
    endDateDp: any;
    firstPayDateDp: any;
    nextPayDateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private contractService: ContractService,
        private mentorService: MentorService,
        private courseService: CourseService,
        private agreementService: AgreementService,
        private courseTypeService: CourseTypeService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ contract }) => {
            this.contract = contract;
        });
        if (this.contract.agreementId == null) {
            this.contract.agreementId = +this.activatedRoute.snapshot.paramMap.get('agreementId');
            this.agreementService.find(this.contract.agreementId).subscribe(
                (res: HttpResponse<IAgreement>) => {
                    this.courseTypeService.find(res.body.courseType.id).subscribe(
                        (subRes: HttpResponse<ICourseType>) => {
                            this.courseType = subRes.body.type.concat(' (' + subRes.body.location + ')');
                            this.contract.price = subRes.body.price;
                            this.contract.mentorRate = subRes.body.mentorRate;
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        }
        this.mentorService.query({ filter: 'contract-is-null' }).subscribe(
            (res: HttpResponse<IMentor[]>) => {
                if (!this.contract.mentorId) {
                    this.mentors = res.body;
                } else {
                    this.mentorService.find(this.contract.mentorId).subscribe(
                        (subRes: HttpResponse<IMentor>) => {
                            this.mentors = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.agreementService.query().subscribe(
            (res: HttpResponse<IAgreement[]>) => {
                this.agreements = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.contract.id !== undefined) {
            this.subscribeToSaveResponse(this.contractService.update(this.contract));
        } else {
            this.subscribeToSaveResponse(this.contractService.create(this.contract));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IContract>>) {
        result.subscribe((res: HttpResponse<IContract>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackAgreementById(index: number, item: IAgreement) {
        return item.id;
    }
    get contract() {
        return this._contract;
    }

    set contract(contract: IContract) {
        this._contract = contract;
    }
}
