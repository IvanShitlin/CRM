import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICourseType } from 'app/shared/model/course-type.model';

type EntityResponseType = HttpResponse<ICourseType>;
type EntityArrayResponseType = HttpResponse<ICourseType[]>;

@Injectable({ providedIn: 'root' })
export class CourseTypeService {
    private resourceUrl = SERVER_API_URL + 'api/course-types';

    constructor(private http: HttpClient) {}

    create(courseType: ICourseType): Observable<EntityResponseType> {
        return this.http.post<ICourseType>(this.resourceUrl, courseType, { observe: 'response' });
    }

    update(courseType: ICourseType): Observable<EntityResponseType> {
        return this.http.put<ICourseType>(this.resourceUrl, courseType, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICourseType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICourseType[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
