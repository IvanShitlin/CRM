import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Agreement } from 'app/shared/model/agreement.model';
import { AgreementService } from './agreement.service';
import { AgreementComponent } from './agreement.component';
import { AgreementDetailComponent } from './agreement-detail.component';
import { AgreementUpdateComponent } from './agreement-update.component';
import { AgreementDeletePopupComponent } from './agreement-delete-dialog.component';
import { IAgreement } from 'app/shared/model/agreement.model';

@Injectable({ providedIn: 'root' })
export class AgreementResolve implements Resolve<IAgreement> {
    constructor(private service: AgreementService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((agreement: HttpResponse<Agreement>) => agreement.body));
        }
        return of(new Agreement());
    }
}

export const agreementRoute: Routes = [
    {
        path: 'agreement',
        component: AgreementComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            defaultSort: 'id,asc',
            pageTitle: 'hipsterfoxApp.agreement.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'agreement/:id/view',
        component: AgreementDetailComponent,
        resolve: {
            agreement: AgreementResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.agreement.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'agreement/new',
        component: AgreementUpdateComponent,
        resolve: {
            agreement: AgreementResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.agreement.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'client/:clientId/agreement/new',
        component: AgreementUpdateComponent,
        resolve: {
            agreement: AgreementResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.agreement.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'agreement/:id/edit',
        component: AgreementUpdateComponent,
        resolve: {
            agreement: AgreementResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.agreement.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const agreementPopupRoute: Routes = [
    {
        path: 'agreement/:id/delete',
        component: AgreementDeletePopupComponent,
        resolve: {
            agreement: AgreementResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.agreement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
