import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';

import { HipsterfoxSharedModule } from '../../shared';
import { ElasticsearchReindexComponent, ElasticsearchReindexModalComponent } from 'app/admin';
import { ElasticsearchReindexService } from 'app/admin/elasticsearch-reindex/elasticsearch-reindex.service';

@NgModule({
    imports: [HipsterfoxSharedModule],
    declarations: [ElasticsearchReindexComponent, ElasticsearchReindexModalComponent],
    entryComponents: [ElasticsearchReindexModalComponent],
    providers: [ElasticsearchReindexService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HipsterfoxElasticsearchReindexModule {}
