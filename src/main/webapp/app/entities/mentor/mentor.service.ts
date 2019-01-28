import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMentor } from 'app/shared/model/mentor.model';

type EntityResponseType = HttpResponse<IMentor>;
type EntityArrayResponseType = HttpResponse<IMentor[]>;

@Injectable({ providedIn: 'root' })
export class MentorService {
    private resourceUrl = SERVER_API_URL + 'api/mentors';

    constructor(private http: HttpClient) {}

    create(mentor: IMentor): Observable<EntityResponseType> {
        return this.http.post<IMentor>(this.resourceUrl, mentor, { observe: 'response' });
    }

    update(mentor: IMentor): Observable<EntityResponseType> {
        return this.http.put<IMentor>(this.resourceUrl, mentor, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IMentor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMentor[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
