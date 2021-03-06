/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { HipsterfoxTestModule } from '../../../test.module';
import { SalaryItemDeleteDialogComponent } from 'app/entities/salary-item/salary-item-delete-dialog.component';
import { SalaryItemService } from 'app/entities/salary-item/salary-item.service';

describe('Component Tests', () => {
    describe('SalaryItem Management Delete Component', () => {
        let comp: SalaryItemDeleteDialogComponent;
        let fixture: ComponentFixture<SalaryItemDeleteDialogComponent>;
        let service: SalaryItemService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HipsterfoxTestModule],
                declarations: [SalaryItemDeleteDialogComponent]
            })
                .overrideTemplate(SalaryItemDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SalaryItemDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SalaryItemService);
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
