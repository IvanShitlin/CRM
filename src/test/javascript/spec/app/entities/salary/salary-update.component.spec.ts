/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { HipsterfoxTestModule } from '../../../test.module';
import { SalaryUpdateComponent } from 'app/entities/salary/salary-update.component';
import { SalaryService } from 'app/entities/salary/salary.service';
import { Salary } from 'app/shared/model/salary.model';

describe('Component Tests', () => {
    describe('Salary Management Update Component', () => {
        let comp: SalaryUpdateComponent;
        let fixture: ComponentFixture<SalaryUpdateComponent>;
        let service: SalaryService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HipsterfoxTestModule],
                declarations: [SalaryUpdateComponent]
            })
                .overrideTemplate(SalaryUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SalaryUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SalaryService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Salary(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.salary = entity;
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
                    const entity = new Salary();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.salary = entity;
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
