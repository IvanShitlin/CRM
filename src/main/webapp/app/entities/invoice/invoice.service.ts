import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IInvoice } from 'app/shared/model/invoice.model';

type EntityResponseType = HttpResponse<IInvoice>;
type EntityArrayResponseType = HttpResponse<IInvoice[]>;

@Injectable({ providedIn: 'root' })
export class InvoiceService {
    private resourceUrl = SERVER_API_URL + 'api/invoices';

    constructor(private http: HttpClient) {}

    create(invoice: IInvoice): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(invoice);
        return this.http
            .post<IInvoice>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(invoice: IInvoice): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(invoice);
        return this.http
            .put<IInvoice>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IInvoice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IInvoice[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(invoice: IInvoice): IInvoice {
        const copy: IInvoice = Object.assign({}, invoice, {
            dateFrom: invoice.dateFrom != null && invoice.dateFrom.isValid() ? invoice.dateFrom.format(DATE_FORMAT) : null,
            dateTo: invoice.dateTo != null && invoice.dateTo.isValid() ? invoice.dateTo.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.dateFrom = res.body.dateFrom != null ? moment(res.body.dateFrom) : null;
        res.body.dateTo = res.body.dateTo != null ? moment(res.body.dateTo) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((invoice: IInvoice) => {
            invoice.dateFrom = invoice.dateFrom != null ? moment(invoice.dateFrom) : null;
            invoice.dateTo = invoice.dateTo != null ? moment(invoice.dateTo) : null;
        });
        return res;
    }
}
