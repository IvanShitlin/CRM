import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClient } from 'app/shared/model/client.model';
import { AgreementService } from 'app/entities/agreement';
import { IAgreement } from 'app/shared/model/agreement.model';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { JhiAlertService } from 'ng-jhipster';

@Component({
    selector: 'jhi-client-detail',
    templateUrl: './client-detail.component.html'
})
export class ClientDetailComponent implements OnInit {
    client: IClient;
    agreements: IAgreement[];

    constructor(
        private activatedRoute: ActivatedRoute,
        private agreementService: AgreementService,
        private jhiAlertService: JhiAlertService
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ client }) => {
            this.client = client;
        });
        const criteria = [{ key: 'clientId.equals', value: this.client.id }];
        this.agreementService
            .query({
                criteria
            })
            .subscribe(
                (res: HttpResponse<IAgreement[]>) => this.paginateAgreements(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    previousState() {
        window.history.back();
    }

    private paginateAgreements(data: IAgreement[], headers: HttpHeaders) {
        this.agreements = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
