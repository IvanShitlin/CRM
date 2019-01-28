/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HipsterfoxTestModule } from '../../../test.module';
import { SalaryItemDetailComponent } from 'app/entities/salary-item/salary-item-detail.component';
import { SalaryItem } from 'app/shared/model/salary-item.model';

describe('Component Tests', () => {
    describe('SalaryItem Management Detail Component', () => {
        let comp: SalaryItemDetailComponent;
        let fixture: ComponentFixture<SalaryItemDetailComponent>;
        const route = ({ data: of({ salaryItem: new SalaryItem(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HipsterfoxTestModule],
                declarations: [SalaryItemDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SalaryItemDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SalaryItemDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.salaryItem).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
