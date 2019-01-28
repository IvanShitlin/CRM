import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { JhiIdPureFilterPipe } from './util/id-pure-filter';
import { HipsterfoxSharedLibsModule, HipsterfoxSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
    imports: [HipsterfoxSharedLibsModule, HipsterfoxSharedCommonModule],
    declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective, JhiIdPureFilterPipe],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [JhiLoginModalComponent],
    exports: [HipsterfoxSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective, JhiIdPureFilterPipe],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HipsterfoxSharedModule {
    static forRoot() {
        return {
            ngModule: HipsterfoxSharedModule
        };
    }
}
