import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IMentor } from 'app/shared/model/mentor.model';
import { MentorService } from './mentor.service';
import { ICourse } from 'app/shared/model/course.model';
import { CourseService } from 'app/entities/course';

@Component({
    selector: 'jhi-mentor-update',
    templateUrl: './mentor-update.component.html'
})
export class MentorUpdateComponent implements OnInit {
    private _mentor: IMentor;
    isSaving: boolean;
    allCourses: ICourse[];
    selectedCourses: ICourse[];
    mentorCriteria: object;

    constructor(private mentorService: MentorService, private activatedRoute: ActivatedRoute, private courseService: CourseService) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ mentor }) => {
            this.mentor = mentor;
        });
        this.courseService.query().subscribe((result: HttpResponse<ICourse[]>) => (this.allCourses = result.body));
        this.mentorCriteria = [{ key: 'mentorsId.equals', value: this.mentor.id }];
        this.initSelectedCourses();
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.mentor.id !== undefined) {
            this.subscribeToSaveResponse(this.mentorService.update(this.mentor));
        } else {
            this.subscribeToSaveResponse(this.mentorService.create(this.mentor));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMentor>>) {
        result.subscribe((res: HttpResponse<IMentor>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get mentor() {
        return this._mentor;
    }

    set mentor(mentor: IMentor) {
        this._mentor = mentor;
    }

    assignMentor(mentor: IMentor, course: ICourse) {
        this.courseService.assignMentor(mentor.id, course.id).subscribe(() => this.initSelectedCourses());
    }

    deleteMentor(mentor: IMentor, course: ICourse) {
        this.courseService.deleteMentor(mentor.id, course.id).subscribe(() => this.initSelectedCourses());
    }

    private initSelectedCourses() {
        this.courseService
            .query({ criteria: this.mentorCriteria })
            .subscribe((result: HttpResponse<ICourse[]>) => (this.selectedCourses = result.body));
    }

    public isSelected(course: ICourse) {
        let isContains = false;
        this.selectedCourses.map(e => {
            if (e.id === course.id) {
                isContains = true;
            }
        });
        return isContains;
    }
}
