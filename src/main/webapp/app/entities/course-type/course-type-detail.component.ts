import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICourseType } from 'app/shared/model/course-type.model';

@Component({
    selector: 'jhi-course-type-detail',
    templateUrl: './course-type-detail.component.html'
})
export class CourseTypeDetailComponent implements OnInit {
    courseType: ICourseType;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ courseType }) => {
            this.courseType = courseType;
        });
    }

    previousState() {
        window.history.back();
    }
}
