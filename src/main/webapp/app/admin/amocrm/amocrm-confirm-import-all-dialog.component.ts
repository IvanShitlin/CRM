import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { AmocrmService } from 'app/admin/amocrm/amocrm.service';

@Component({
    selector: 'jhi-amocrm-confirm-import-all-dialog',
    templateUrl: './amocrm-confirm-import-all-dialog.component.html'
})
export class AmocrmConfirmImportAllDialogComponent {
    isImporting: boolean;

    constructor(private amocrmService: AmocrmService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmImportAll() {
        this.isImporting = true;
        this.amocrmService.importAllClients().subscribe(response => {
            this.eventManager.broadcast({
                name: 'amocrmImportAllClients',
                content: 'Imported all clients from Amocrm. Total amount: ',
                amount: response.body
            });
            this.activeModal.dismiss(true);
        });
        this.isImporting = false;
    }
}
