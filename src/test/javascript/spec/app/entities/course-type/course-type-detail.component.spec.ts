/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HipsterfoxTestModule } from '../../../test.module';
import { CourseTypeDetailComponent } from 'app/entities/course-type/course-type-detail.component';
import { CourseType } from 'app/shared/model/course-type.model';

describe('Component Tests', () => {
    describe('CourseType Management Detail Component', () => {
        let comp: CourseTypeDetailComponent;
        let fixture: ComponentFixture<CourseTypeDetailComponent>;
        const route = ({ data: of({ courseType: new CourseType(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HipsterfoxTestModule],
                declarations: [CourseTypeDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CourseTypeDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CourseTypeDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.courseType).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
