import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { IMentor, Mentor } from 'app/shared/model/mentor.model';
import { MentorService } from './mentor.service';
import { MentorComponent } from './mentor.component';
import { MentorDetailComponent } from './mentor-detail.component';
import { MentorUpdateComponent } from './mentor-update.component';
import { MentorDeletePopupComponent } from './mentor-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class MentorResolve implements Resolve<IMentor> {
    constructor(private service: MentorService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((mentor: HttpResponse<Mentor>) => mentor.body));
        }
        return of(new Mentor());
    }
}

export const mentorRoute: Routes = [
    {
        path: 'mentor',
        component: MentorComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            defaultSort: 'id,asc',
            pageTitle: 'hipsterfoxApp.mentor.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'mentor/:id/view',
        component: MentorDetailComponent,
        resolve: {
            mentor: MentorResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.mentor.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'mentor/new',
        component: MentorUpdateComponent,
        resolve: {
            mentor: MentorResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.mentor.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'mentor/:id/edit',
        component: MentorUpdateComponent,
        resolve: {
            mentor: MentorResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.mentor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const mentorPopupRoute: Routes = [
    {
        path: 'mentor/:id/delete',
        component: MentorDeletePopupComponent,
        resolve: {
            mentor: MentorResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_MANAGER'],
            pageTitle: 'hipsterfoxApp.mentor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
