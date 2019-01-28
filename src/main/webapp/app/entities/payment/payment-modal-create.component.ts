import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IPayment, Payment } from 'app/shared/model/payment.model';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { PaymentService } from 'app/entities/payment/payment.service';
import { InvoiceService } from 'app/entities/invoice';
import * as moment from 'moment';

@Component({
    selector: 'jhi-payment-new',
    templateUrl: './payment-modal-create.component.html'
})
export class PaymentModalCreateComponent implements OnInit {
    payment: IPayment;
    isSaving: boolean;

    constructor(
        private invoiceService: InvoiceService,
        private jhiAlertService: JhiAlertService,
        private paymentService: PaymentService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        this.subscribeToSaveResponse(this.paymentService.create(this.payment));
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Payment>>) {
        result.subscribe((res: HttpResponse<Payment>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.activeModal.dismiss(true);
        this.eventManager.broadcast({
            name: 'invoiceAddPayment' + this.payment.invoiceId,
            content: 'Create a payment'
        });
    }

    private onSaveError() {
        this.isSaving = false;
    }

    ngOnInit(): void {
        this.invoiceService
            .find(this.payment.invoiceId)
            .subscribe(data => ((this.payment.sum.amount = data.body.sum.amount), (this.payment.sum.currency = data.body.sum.currency)));
        this.payment.date = moment();
    }
}

@Component({
    selector: 'jhi-payment-new-popup',
    template: ''
})
export class PaymentNewPopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;
    private payment: Payment;

    constructor(
        private jhiAlertService: JhiAlertService,
        private paymentService: PaymentService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private modalService: NgbModal
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ payment }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PaymentModalCreateComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.payment = payment;
                this.ngbModalRef.componentInstance.payment.invoiceId = +this.activatedRoute.snapshot.paramMap.get('invoiceId');
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
