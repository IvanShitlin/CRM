import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ICourseType } from 'app/shared/model/course-type.model';
import { CourseTypeService } from './course-type.service';

@Component({
    selector: 'jhi-course-type-update',
    templateUrl: './course-type-update.component.html'
})
export class CourseTypeUpdateComponent implements OnInit {
    private _courseType: ICourseType;
    isSaving: boolean;

    constructor(private courseTypeService: CourseTypeService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ courseType }) => {
            this.courseType = courseType;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.courseType.id !== undefined) {
            this.subscribeToSaveResponse(this.courseTypeService.update(this.courseType));
        } else {
            this.subscribeToSaveResponse(this.courseTypeService.create(this.courseType));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICourseType>>) {
        result.subscribe((res: HttpResponse<ICourseType>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    get courseType() {
        return this._courseType;
    }

    set courseType(courseType: ICourseType) {
        this._courseType = courseType;
    }
}
