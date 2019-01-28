import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDebtorView } from 'app/shared/model/debtor-view.model';

type EntityResponseType = HttpResponse<IDebtorView>;
type EntityArrayResponseType = HttpResponse<IDebtorView[]>;

@Injectable({ providedIn: 'root' })
export class DebtorViewService {
    private resourceUrl = SERVER_API_URL + 'api/debtors';

    constructor(private http: HttpClient) {}

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDebtorView[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((debtor: IDebtorView) => {
            debtor.paymentDate = debtor.paymentDate != null ? moment(debtor.paymentDate) : null;
        });
        return res;
    }
}
