import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISalaryItem } from 'app/shared/model/salary-item.model';
import { SalaryItemService } from './salary-item.service';

@Component({
    selector: 'jhi-salary-item-delete-dialog',
    templateUrl: './salary-item-delete-dialog.component.html'
})
export class SalaryItemDeleteDialogComponent {
    salaryItem: ISalaryItem;

    constructor(private salaryItemService: SalaryItemService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.salaryItemService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'salaryItemListModification',
                content: 'Deleted an salaryItem'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-salary-item-delete-popup',
    template: ''
})
export class SalaryItemDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ salaryItem }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SalaryItemDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.salaryItem = salaryItem;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
