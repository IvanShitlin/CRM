import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HipsterfoxSharedModule } from 'app/shared';
import {
    PaymentComponent,
    PaymentDetailComponent,
    PaymentUpdateComponent,
    PaymentDeletePopupComponent,
    PaymentDeleteDialogComponent,
    paymentRoute,
    paymentPopupRoute
} from './';
import { PaymentModalCreateComponent, PaymentNewPopupComponent } from './payment-modal-create.component';

const ENTITY_STATES = [...paymentRoute, ...paymentPopupRoute];

@NgModule({
    imports: [HipsterfoxSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PaymentComponent,
        PaymentDetailComponent,
        PaymentUpdateComponent,
        PaymentDeleteDialogComponent,
        PaymentDeletePopupComponent,
        PaymentModalCreateComponent,
        PaymentNewPopupComponent
    ],
    entryComponents: [
        PaymentComponent,
        PaymentUpdateComponent,
        PaymentDeleteDialogComponent,
        PaymentDeletePopupComponent,
        PaymentModalCreateComponent,
        PaymentNewPopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HipsterfoxPaymentModule {}
