import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DiffMatchPatchModule } from 'ng-diff-match-patch-att';

import { HipsterfoxSharedModule } from '../../shared';
import { EntityAuditComponent } from './entity-audit.component';
import { EntityAuditModalComponent } from './entity-audit-modal.component';
import { EntityAuditService } from './entity-audit.service';

@NgModule({
    imports: [CommonModule, HipsterfoxSharedModule, DiffMatchPatchModule],
    declarations: [EntityAuditComponent, EntityAuditModalComponent],
    entryComponents: [EntityAuditModalComponent],
    providers: [EntityAuditService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EntityAuditModule {}
