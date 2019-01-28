/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { HipsterfoxTestModule } from '../../../test.module';
import { SalaryDeleteDialogComponent } from 'app/entities/salary/salary-delete-dialog.component';
import { SalaryService } from 'app/entities/salary/salary.service';

describe('Component Tests', () => {
    describe('Salary Management Delete Component', () => {
        let comp: SalaryDeleteDialogComponent;
        let fixture: ComponentFixture<SalaryDeleteDialogComponent>;
        let service: SalaryService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HipsterfoxTestModule],
                declarations: [SalaryDeleteDialogComponent]
            })
                .overrideTemplate(SalaryDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SalaryDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SalaryService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
