import { Routes } from '@angular/router';
import { AmocrmComponent } from 'app/admin/amocrm/amocrm.component';

export const amocrmRoute: Routes = [
    {
        path: 'amocrm-configuration',
        component: AmocrmComponent,
        data: {
            pageTitle: 'amocrm.title'
        }
    }
];
