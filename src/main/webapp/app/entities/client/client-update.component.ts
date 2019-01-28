import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IClient } from 'app/shared/model/client.model';
import { ClientService } from './client.service';
import { EMAIL_INVALID_FORMAT_TYPE } from 'app/shared';

@Component({
    selector: 'jhi-client-update',
    templateUrl: './client-update.component.html'
})
export class ClientUpdateComponent implements OnInit {
    private _client: IClient;
    isSaving: boolean;
    errorEmailInvalidFormat: string;

    constructor(private clientService: ClientService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ client }) => {
            this.client = client;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.client.id !== undefined) {
            this.subscribeToSaveResponse(this.clientService.update(this.client));
        } else {
            this.subscribeToSaveResponse(this.clientService.create(this.client));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IClient>>) {
        result.subscribe(
            (response: HttpResponse<IClient>) => this.onSaveSuccess(),
            (response: HttpErrorResponse) => this.onSaveError(response)
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError(response: HttpErrorResponse) {
        this.isSaving = false;
        if (response.status === 400 && response.error.type === EMAIL_INVALID_FORMAT_TYPE) {
            this.errorEmailInvalidFormat = 'ERROR';
        }
    }
    get client() {
        return this._client;
    }

    set client(client: IClient) {
        this._client = client;
    }
}
