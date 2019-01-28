import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAgreement } from 'app/shared/model/agreement.model';
import { ContractService } from 'app/entities/contract';
import { IContract } from 'app/shared/model/contract.model';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { JhiAlertService } from 'ng-jhipster';
import { MentorService } from '../mentor';
import { IMentor } from 'app/shared/model/mentor.model';

@Component({
    selector: 'jhi-agreement-detail',
    templateUrl: './agreement-detail.component.html'
})
export class AgreementDetailComponent implements OnInit {
    agreement: IAgreement;
    contracts: IContract[];
    mentors: IMentor[] = [];

    constructor(
        private activatedRoute: ActivatedRoute,
        private jhiAlertService: JhiAlertService,
        private contractService: ContractService,
        private mentorService: MentorService
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ agreement }) => {
            this.agreement = agreement;
        });
        const criteria = [{ key: 'agreementId.equals', value: this.agreement.id }];
        this.contractService
            .query({
                criteria
            })
            .subscribe(
                (res: HttpResponse<IContract[]>) => {
                    this.paginateContracts(res.body, res.headers);
                    this.populateMentors(res.body);
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    previousState() {
        window.history.back();
    }

    // TODO Add pagination in future
    private paginateContracts(data: IContract[], headers: HttpHeaders) {
        this.contracts = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    populateMentors(contracts: IContract[]) {
        contracts.forEach(contract => {
            this.mentorService.find(contract.mentorId).subscribe((res: HttpResponse<IMentor>) => {
                this.mentors.push(res.body);
            });
        });
    }

    getMentorFullName(id: number) {
        let mentor: IMentor;
        mentor = this.mentors.find((item: IMentor) => item.id === id);
        if (!mentor) {
            return '';
        }
        return mentor.lastName + ' ' + mentor.firstName;
    }
}
