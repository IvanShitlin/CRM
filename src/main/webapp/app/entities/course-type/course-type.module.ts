import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HipsterfoxSharedModule } from 'app/shared';
import {
    CourseTypeComponent,
    CourseTypeDetailComponent,
    CourseTypeUpdateComponent,
    CourseTypeDeletePopupComponent,
    CourseTypeDeleteDialogComponent,
    courseTypeRoute,
    courseTypePopupRoute
} from './';

const ENTITY_STATES = [...courseTypeRoute, ...courseTypePopupRoute];

@NgModule({
    imports: [HipsterfoxSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CourseTypeComponent,
        CourseTypeDetailComponent,
        CourseTypeUpdateComponent,
        CourseTypeDeleteDialogComponent,
        CourseTypeDeletePopupComponent
    ],
    entryComponents: [CourseTypeComponent, CourseTypeUpdateComponent, CourseTypeDeleteDialogComponent, CourseTypeDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HipsterfoxCourseTypeModule {}
