import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMentor } from 'app/shared/model/mentor.model';
import { ICourse } from 'app/shared/model/course.model';
import { HttpResponse } from '@angular/common/http';
import { CourseService } from 'app/entities/course';

@Component({
    selector: 'jhi-mentor-detail',
    templateUrl: './mentor-detail.component.html'
})
export class MentorDetailComponent implements OnInit {
    mentor: IMentor;
    courses: ICourse[];

    constructor(private activatedRoute: ActivatedRoute, private courseService: CourseService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mentor }) => {
            this.mentor = mentor;
        });
        const criteria = [{ key: 'mentorsId.equals', value: this.mentor.id }];
        this.courseService.query({ criteria }).subscribe((result: HttpResponse<ICourse[]>) => (this.courses = result.body));
    }

    previousState() {
        window.history.back();
    }
}
