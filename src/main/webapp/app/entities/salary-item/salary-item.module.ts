import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HipsterfoxSharedModule } from 'app/shared';
import {
    SalaryItemComponent,
    SalaryItemDetailComponent,
    SalaryItemUpdateComponent,
    SalaryItemDeletePopupComponent,
    SalaryItemDeleteDialogComponent,
    salaryItemRoute,
    salaryItemPopupRoute
} from './';

const ENTITY_STATES = [...salaryItemRoute, ...salaryItemPopupRoute];

@NgModule({
    imports: [HipsterfoxSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SalaryItemComponent,
        SalaryItemDetailComponent,
        SalaryItemUpdateComponent,
        SalaryItemDeleteDialogComponent,
        SalaryItemDeletePopupComponent
    ],
    entryComponents: [SalaryItemComponent, SalaryItemUpdateComponent, SalaryItemDeleteDialogComponent, SalaryItemDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HipsterfoxSalaryItemModule {}
