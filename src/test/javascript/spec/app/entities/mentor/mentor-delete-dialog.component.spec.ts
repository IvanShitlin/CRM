/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { HipsterfoxTestModule } from '../../../test.module';
import { MentorDeleteDialogComponent } from 'app/entities/mentor/mentor-delete-dialog.component';
import { MentorService } from 'app/entities/mentor/mentor.service';

describe('Component Tests', () => {
    describe('Mentor Management Delete Component', () => {
        let comp: MentorDeleteDialogComponent;
        let fixture: ComponentFixture<MentorDeleteDialogComponent>;
        let service: MentorService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HipsterfoxTestModule],
                declarations: [MentorDeleteDialogComponent]
            })
                .overrideTemplate(MentorDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MentorDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MentorService);
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
