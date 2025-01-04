import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ReferenceComponent } from './list/reference.component';
import { ReferenceDetailComponent } from './detail/reference-detail.component';
import { ReferenceUpdateComponent } from './update/reference-update.component';
import ReferenceResolve from './route/reference-routing-resolve.service';

const referenceRoute: Routes = [
  {
    path: '',
    component: ReferenceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReferenceDetailComponent,
    resolve: {
      reference: ReferenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReferenceUpdateComponent,
    resolve: {
      reference: ReferenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReferenceUpdateComponent,
    resolve: {
      reference: ReferenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default referenceRoute;
