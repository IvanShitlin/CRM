/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { HipsterfoxTestModule } from '../../../test.module';
import { CourseTypeUpdateComponent } from 'app/entities/course-type/course-type-update.component';
import { CourseTypeService } from 'app/entities/course-type/course-type.service';
import { CourseType } from 'app/shared/model/course-type.model';

describe('Component Tests', () => {
    describe('CourseType Management Update Component', () => {
        let comp: CourseTypeUpdateComponent;
        let fixture: ComponentFixture<CourseTypeUpdateComponent>;
        let service: CourseTypeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HipsterfoxTestModule],
                declarations: [CourseTypeUpdateComponent]
            })
                .overrideTemplate(CourseTypeUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CourseTypeUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CourseTypeService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new CourseType(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.courseType = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new CourseType();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.courseType = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
