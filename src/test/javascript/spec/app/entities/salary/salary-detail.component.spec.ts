/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HipsterfoxTestModule } from '../../../test.module';
import { SalaryDetailComponent } from 'app/entities/salary/salary-detail.component';
import { Salary } from 'app/shared/model/salary.model';

describe('Component Tests', () => {
    describe('Salary Management Detail Component', () => {
        let comp: SalaryDetailComponent;
        let fixture: ComponentFixture<SalaryDetailComponent>;
        const route = ({ data: of({ salary: new Salary(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HipsterfoxTestModule],
                declarations: [SalaryDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SalaryDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SalaryDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.salary).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
