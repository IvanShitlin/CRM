import { Component, OnInit } from '@angular/core';
import { AmocrmService } from 'app/admin/amocrm/amocrm.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AmocrmConfirmImportAllDialogComponent } from 'app/admin';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

@Component({
    selector: 'jhi-amocrm',
    templateUrl: './amocrm.component.html'
})
export class AmocrmComponent implements OnInit {
    importedClients = '';
    isImporting: boolean;
    syncedClients = '';

    constructor(
        private amocrmService: AmocrmService,
        private eventManager: JhiEventManager,
        private alertService: JhiAlertService,
        private modalService: NgbModal
    ) {}

    ngOnInit() {
        this.registerImportClients();
    }

    importAllClients() {
        this.isImporting = true;
        const modalRef = this.modalService.open(AmocrmConfirmImportAllDialogComponent, { size: 'lg', backdrop: 'static' });
        modalRef.result.then(
            result => {
                // Left blank intentionally, nothing to do here
            },
            reason => {
                // Left blank intentionally, nothing to do here
            }
        );
        this.isImporting = false;
    }

    registerImportClients() {
        this.eventManager.subscribe('amocrmImportAllClients', response => this.onImport(response));
    }

    private onImport(response) {
        this.importedClients = response.amount;
        this.alertService.success(response.content + response.amount);
    }

    syncAllClients() {
        this.isImporting = true;
        this.amocrmService.syncAllClients().subscribe(response => {
            this.syncedClients = response.body;
        });
        this.isImporting = false;
    }
}
