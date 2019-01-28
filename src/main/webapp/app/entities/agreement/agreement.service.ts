import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAgreement } from 'app/shared/model/agreement.model';

type EntityResponseType = HttpResponse<IAgreement>;
type EntityArrayResponseType = HttpResponse<IAgreement[]>;

@Injectable({ providedIn: 'root' })
export class AgreementService {
    public resourceUrl = SERVER_API_URL + 'api/agreements';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/agreements';

    constructor(private http: HttpClient) {}

    create(agreement: IAgreement): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(agreement);
        return this.http
            .post<IAgreement>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(agreement: IAgreement): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(agreement);
        return this.http
            .put<IAgreement>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IAgreement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAgreement[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAgreement[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(agreement: IAgreement): IAgreement {
        const copy: IAgreement = Object.assign({}, agreement, {
            startDate: agreement.startDate != null && agreement.startDate.isValid() ? agreement.startDate.format(DATE_FORMAT) : null,
            endDate: agreement.endDate != null && agreement.endDate.isValid() ? agreement.endDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.startDate = res.body.startDate != null ? moment(res.body.startDate) : null;
        res.body.endDate = res.body.endDate != null ? moment(res.body.endDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((agreement: IAgreement) => {
            agreement.startDate = agreement.startDate != null ? moment(agreement.startDate) : null;
            agreement.endDate = agreement.endDate != null ? moment(agreement.endDate) : null;
        });
        return res;
    }
}
