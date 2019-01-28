/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { HipsterfoxTestModule } from '../../../test.module';
import { MentorUpdateComponent } from 'app/entities/mentor/mentor-update.component';
import { MentorService } from 'app/entities/mentor/mentor.service';
import { Mentor } from 'app/shared/model/mentor.model';

describe('Component Tests', () => {
    describe('Mentor Management Update Component', () => {
        let comp: MentorUpdateComponent;
        let fixture: ComponentFixture<MentorUpdateComponent>;
        let service: MentorService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HipsterfoxTestModule],
                declarations: [MentorUpdateComponent]
            })
                .overrideTemplate(MentorUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(MentorUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MentorService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Mentor(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.mentor = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Mentor();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.mentor = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
