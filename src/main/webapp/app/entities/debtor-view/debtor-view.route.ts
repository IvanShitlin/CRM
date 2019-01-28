import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { DebtorView, IDebtorView } from 'app/shared/model/debtor-view.model';
import { DebtorViewService } from './debtor-view.service';
import { DebtorViewComponent } from './debtor-view.component';

@Injectable({ providedIn: 'root' })
export class DebtorViewResolve implements Resolve<IDebtorView> {
    constructor(private service: DebtorViewService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        return of(new DebtorView());
    }
}

export const debtorViewRoute: Routes = [
    {
        path: 'debtors',
        component: DebtorViewComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            defaultSort: 'id,asc',
            pageTitle: 'hipsterfoxApp.debtor-view.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
