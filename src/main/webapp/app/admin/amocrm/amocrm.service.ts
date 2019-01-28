import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class AmocrmService {
    constructor(private http: HttpClient) {}

    importAllClients(): Observable<HttpResponse<any>> {
        return this.http.get(SERVER_API_URL + 'api/amocrm/clients/import/all', { observe: 'response' });
    }

    syncAllClients(): Observable<HttpResponse<any>> {
        return this.http.get(SERVER_API_URL + 'api/amocrm/clients/sync/all', { observe: 'response' });
    }
}
