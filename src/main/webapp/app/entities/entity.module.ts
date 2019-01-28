import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { HipsterfoxClientModule } from './client/client.module';
import { HipsterfoxMentorModule } from './mentor/mentor.module';
import { HipsterfoxCourseModule } from './course/course.module';
import { HipsterfoxAgreementModule } from './agreement/agreement.module';
import { HipsterfoxContractModule } from './contract/contract.module';
import { HipsterfoxInvoiceModule } from './invoice/invoice.module';
import { HipsterfoxPaymentModule } from './payment/payment.module';
import { HipsterfoxSalaryItemModule } from './salary-item/salary-item.module';
import { HipsterfoxSalaryModule } from './salary/salary.module';
import { HipsterfoxDebtorViewModule } from './debtor-view/debtor-view.module';
import { HipsterfoxTaskModule } from './task/task.module';
import { HipsterfoxCourseTypeModule } from './course-type/course-type.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        HipsterfoxClientModule,
        HipsterfoxMentorModule,
        HipsterfoxCourseModule,
        HipsterfoxAgreementModule,
        HipsterfoxContractModule,
        HipsterfoxInvoiceModule,
        HipsterfoxPaymentModule,
        HipsterfoxSalaryItemModule,
        HipsterfoxSalaryModule,
        HipsterfoxDebtorViewModule,
        HipsterfoxTaskModule,
        HipsterfoxCourseTypeModule,
        HipsterfoxDebtorViewModule
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HipsterfoxEntityModule {}
