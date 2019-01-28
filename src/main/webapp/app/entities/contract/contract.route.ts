import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Contract } from 'app/shared/model/contract.model';
import { ContractService } from './contract.service';
import { ContractComponent } from './contract.component';
import { ContractDetailComponent } from './contract-detail.component';
import { ContractUpdateComponent } from './contract-update.component';
import { ContractDeletePopupComponent } from './contract-delete-dialog.component';
import { IContract } from 'app/shared/model/contract.model';

@Injectable({ providedIn: 'root' })
export class ContractResolve implements Resolve<IContract> {
    constructor(private service: ContractService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((contract: HttpResponse<Contract>) => contract.body));
        }
        return of(new Contract());
    }
}

export const contractRoute: Routes = [
    {
        path: 'contract',
        component: ContractComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            defaultSort: 'id,asc',
            pageTitle: 'hipsterfoxApp.contract.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'contract/:id/view',
        component: ContractDetailComponent,
        resolve: {
            contract: ContractResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.contract.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'agreement/:agreementId/contract/new',
        component: ContractUpdateComponent,
        resolve: {
            contract: ContractResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.contract.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'contract/:id/edit',
        component: ContractUpdateComponent,
        resolve: {
            contract: ContractResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.contract.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const contractPopupRoute: Routes = [
    {
        path: 'contract/:id/delete',
        component: ContractDeletePopupComponent,
        resolve: {
            contract: ContractResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.contract.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
