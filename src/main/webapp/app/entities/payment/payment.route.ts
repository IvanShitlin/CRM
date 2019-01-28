import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Payment } from 'app/shared/model/payment.model';
import { PaymentService } from './payment.service';
import { PaymentComponent } from './payment.component';
import { PaymentDetailComponent } from './payment-detail.component';
import { PaymentUpdateComponent } from './payment-update.component';
import { PaymentDeletePopupComponent } from './payment-delete-dialog.component';
import { IPayment } from 'app/shared/model/payment.model';
import { PaymentNewPopupComponent } from 'app/entities/payment/payment-modal-create.component';

@Injectable({ providedIn: 'root' })
export class PaymentResolve implements Resolve<IPayment> {
    constructor(private service: PaymentService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((payment: HttpResponse<Payment>) => payment.body));
        }
        return of(new Payment());
    }
}

export const paymentRoute: Routes = [
    {
        path: 'payment',
        component: PaymentComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            defaultSort: 'id,asc',
            pageTitle: 'hipsterfoxApp.payment.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'payment/:id/view',
        component: PaymentDetailComponent,
        resolve: {
            payment: PaymentResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.payment.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'payment/:id/edit',
        component: PaymentUpdateComponent,
        resolve: {
            payment: PaymentResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.payment.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const paymentPopupRoute: Routes = [
    {
        path: 'payment/:id/delete',
        component: PaymentDeletePopupComponent,
        resolve: {
            payment: PaymentResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.payment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'invoice/:invoiceId/payment/new',
        component: PaymentNewPopupComponent,
        resolve: {
            payment: PaymentResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.payment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
