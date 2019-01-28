import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ISalary, Salary } from 'app/shared/model/salary.model';
import { SalaryService } from './salary.service';
import { SalaryComponent } from './salary.component';
import { SalaryDetailComponent } from './salary-detail.component';
import { SalaryUpdateComponent } from './salary-update.component';
import { SalaryDeletePopupComponent } from './salary-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class SalaryResolve implements Resolve<ISalary> {
    constructor(private service: SalaryService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((salary: HttpResponse<Salary>) => salary.body));
        }
        return of(new Salary());
    }
}

export const salaryRoute: Routes = [
    {
        path: 'salary',
        component: SalaryComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            defaultSort: 'id,asc',
            pageTitle: 'hipsterfoxApp.salary.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'salary/:id/view',
        component: SalaryDetailComponent,
        resolve: {
            salary: SalaryResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.salary.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'salary/new',
        component: SalaryUpdateComponent,
        resolve: {
            salary: SalaryResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.salary.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'salary/:id/edit',
        component: SalaryUpdateComponent,
        resolve: {
            salary: SalaryResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.salary.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const salaryPopupRoute: Routes = [
    {
        path: 'salary/:id/delete',
        component: SalaryDeletePopupComponent,
        resolve: {
            salary: SalaryResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.salary.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
