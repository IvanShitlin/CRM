import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IContract } from 'app/shared/model/contract.model';

type EntityResponseType = HttpResponse<IContract>;
type EntityArrayResponseType = HttpResponse<IContract[]>;

@Injectable({ providedIn: 'root' })
export class ContractService {
    private resourceUrl = SERVER_API_URL + 'api/contracts';

    constructor(private http: HttpClient) {}

    create(contract: IContract): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(contract);
        return this.http
            .post<IContract>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(contract: IContract): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(contract);
        return this.http
            .put<IContract>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IContract>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IContract[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(contract: IContract): IContract {
        const copy: IContract = Object.assign({}, contract, {
            startDate: contract.startDate != null && contract.startDate.isValid() ? contract.startDate.format(DATE_FORMAT) : null,
            endDate: contract.endDate != null && contract.endDate.isValid() ? contract.endDate.format(DATE_FORMAT) : null,
            firstPayDate:
                contract.firstPayDate != null && contract.firstPayDate.isValid() ? contract.firstPayDate.format(DATE_FORMAT) : null,
            nextPayDate: contract.nextPayDate != null && contract.nextPayDate.isValid() ? contract.nextPayDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.startDate = res.body.startDate != null ? moment(res.body.startDate) : null;
        res.body.endDate = res.body.endDate != null ? moment(res.body.endDate) : null;
        res.body.firstPayDate = res.body.firstPayDate != null ? moment(res.body.firstPayDate) : null;
        res.body.nextPayDate = res.body.nextPayDate != null ? moment(res.body.nextPayDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((contract: IContract) => {
            contract.startDate = contract.startDate != null ? moment(contract.startDate) : null;
            contract.endDate = contract.endDate != null ? moment(contract.endDate) : null;
            contract.firstPayDate = contract.firstPayDate != null ? moment(contract.firstPayDate) : null;
            contract.nextPayDate = contract.nextPayDate != null ? moment(contract.nextPayDate) : null;
        });
        return res;
    }
}
