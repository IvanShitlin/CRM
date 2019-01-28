import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISalaryItem } from 'app/shared/model/salary-item.model';

type EntityResponseType = HttpResponse<ISalaryItem>;
type EntityArrayResponseType = HttpResponse<ISalaryItem[]>;

@Injectable({ providedIn: 'root' })
export class SalaryItemService {
    private resourceUrl = SERVER_API_URL + 'api/salary-items';

    constructor(private http: HttpClient) {}

    create(salaryItem: ISalaryItem): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(salaryItem);
        return this.http
            .post<ISalaryItem>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(salaryItem: ISalaryItem): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(salaryItem);
        return this.http
            .put<ISalaryItem>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ISalaryItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISalaryItem[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(salaryItem: ISalaryItem): ISalaryItem {
        const copy: ISalaryItem = Object.assign({}, salaryItem, {
            dateFrom: salaryItem.dateFrom != null && salaryItem.dateFrom.isValid() ? salaryItem.dateFrom.format(DATE_FORMAT) : null,
            dateTo: salaryItem.dateTo != null && salaryItem.dateTo.isValid() ? salaryItem.dateTo.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.dateFrom = res.body.dateFrom != null ? moment(res.body.dateFrom) : null;
        res.body.dateTo = res.body.dateTo != null ? moment(res.body.dateTo) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((salaryItem: ISalaryItem) => {
            salaryItem.dateFrom = salaryItem.dateFrom != null ? moment(salaryItem.dateFrom) : null;
            salaryItem.dateTo = salaryItem.dateTo != null ? moment(salaryItem.dateTo) : null;
        });
        return res;
    }
}
