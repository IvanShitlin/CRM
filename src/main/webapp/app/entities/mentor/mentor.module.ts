import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HipsterfoxSharedModule } from 'app/shared';
import {
    MentorComponent,
    MentorDetailComponent,
    MentorUpdateComponent,
    MentorDeletePopupComponent,
    MentorDeleteDialogComponent,
    mentorRoute,
    mentorPopupRoute
} from './';

const ENTITY_STATES = [...mentorRoute, ...mentorPopupRoute];

@NgModule({
    imports: [HipsterfoxSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [MentorComponent, MentorDetailComponent, MentorUpdateComponent, MentorDeleteDialogComponent, MentorDeletePopupComponent],
    entryComponents: [MentorComponent, MentorUpdateComponent, MentorDeleteDialogComponent, MentorDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HipsterfoxMentorModule {}
