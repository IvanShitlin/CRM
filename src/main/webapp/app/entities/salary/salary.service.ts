import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISalary } from 'app/shared/model/salary.model';

type EntityResponseType = HttpResponse<ISalary>;
type EntityArrayResponseType = HttpResponse<ISalary[]>;

@Injectable({ providedIn: 'root' })
export class SalaryService {
    private resourceUrl = SERVER_API_URL + 'api/salaries';

    constructor(private http: HttpClient) {}

    create(salary: ISalary): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(salary);
        return this.http
            .post<ISalary>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(salary: ISalary): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(salary);
        return this.http
            .put<ISalary>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ISalary>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISalary[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(salary: ISalary): ISalary {
        const copy: ISalary = Object.assign({}, salary, {
            dateFrom: salary.dateFrom != null && salary.dateFrom.isValid() ? salary.dateFrom.format(DATE_FORMAT) : null,
            dateTo: salary.dateTo != null && salary.dateTo.isValid() ? salary.dateTo.format(DATE_FORMAT) : null,
            paidDate: salary.paidDate != null && salary.paidDate.isValid() ? salary.paidDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.dateFrom = res.body.dateFrom != null ? moment(res.body.dateFrom) : null;
        res.body.dateTo = res.body.dateTo != null ? moment(res.body.dateTo) : null;
        res.body.paidDate = res.body.paidDate != null ? moment(res.body.paidDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((salary: ISalary) => {
            salary.dateFrom = salary.dateFrom != null ? moment(salary.dateFrom) : null;
            salary.dateTo = salary.dateTo != null ? moment(salary.dateTo) : null;
            salary.paidDate = salary.paidDate != null ? moment(salary.paidDate) : null;
        });
        return res;
    }
}
