import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { SalaryItem } from 'app/shared/model/salary-item.model';
import { SalaryItemService } from './salary-item.service';
import { SalaryItemComponent } from './salary-item.component';
import { SalaryItemDetailComponent } from './salary-item-detail.component';
import { SalaryItemUpdateComponent } from './salary-item-update.component';
import { SalaryItemDeletePopupComponent } from './salary-item-delete-dialog.component';
import { ISalaryItem } from 'app/shared/model/salary-item.model';

@Injectable({ providedIn: 'root' })
export class SalaryItemResolve implements Resolve<ISalaryItem> {
    constructor(private service: SalaryItemService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((salaryItem: HttpResponse<SalaryItem>) => salaryItem.body));
        }
        return of(new SalaryItem());
    }
}

export const salaryItemRoute: Routes = [
    {
        path: 'salary-item',
        component: SalaryItemComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            defaultSort: 'id,asc',
            pageTitle: 'hipsterfoxApp.salaryItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'salary-item/:id/view',
        component: SalaryItemDetailComponent,
        resolve: {
            salaryItem: SalaryItemResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.salaryItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'salary-item/new',
        component: SalaryItemUpdateComponent,
        resolve: {
            salaryItem: SalaryItemResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.salaryItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'salary-item/:id/edit',
        component: SalaryItemUpdateComponent,
        resolve: {
            salaryItem: SalaryItemResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.salaryItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const salaryItemPopupRoute: Routes = [
    {
        path: 'salary-item/:id/delete',
        component: SalaryItemDeletePopupComponent,
        resolve: {
            salaryItem: SalaryItemResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.salaryItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
