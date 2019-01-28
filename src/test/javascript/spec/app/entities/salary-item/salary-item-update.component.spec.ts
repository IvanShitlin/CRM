/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { HipsterfoxTestModule } from '../../../test.module';
import { SalaryItemUpdateComponent } from 'app/entities/salary-item/salary-item-update.component';
import { SalaryItemService } from 'app/entities/salary-item/salary-item.service';
import { SalaryItem } from 'app/shared/model/salary-item.model';

describe('Component Tests', () => {
    describe('SalaryItem Management Update Component', () => {
        let comp: SalaryItemUpdateComponent;
        let fixture: ComponentFixture<SalaryItemUpdateComponent>;
        let service: SalaryItemService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HipsterfoxTestModule],
                declarations: [SalaryItemUpdateComponent]
            })
                .overrideTemplate(SalaryItemUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SalaryItemUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SalaryItemService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new SalaryItem(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.salaryItem = entity;
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
                    const entity = new SalaryItem();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.salaryItem = entity;
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
