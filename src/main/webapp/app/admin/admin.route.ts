import { Routes } from '@angular/router';

import {
    amocrmRoute,
    auditsRoute,
    configurationRoute,
    docsRoute,
    elasticsearchReindexRoute,
    healthRoute,
    logsRoute,
    metricsRoute,
    userMgmtRoute,
    trackerRoute
} from './';

import { UserRouteAccessService } from 'app/core';
import { entityAuditRoute } from 'app/admin/entity-audit/entity-audit.route';

const ADMIN_ROUTES = [
    auditsRoute,
    configurationRoute,
    docsRoute,
    healthRoute,
    logsRoute,
    elasticsearchReindexRoute,
    ...userMgmtRoute,
    ...amocrmRoute,
    metricsRoute,
    entityAuditRoute,
    trackerRoute
];

export const adminState: Routes = [
    {
        path: '',
        data: {
            authorities: ['ROLE_ADMIN']
        },
        canActivate: [UserRouteAccessService],
        children: ADMIN_ROUTES
    }
];
