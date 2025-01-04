import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CVComponent } from './list/cv.component';
import { CVDetailComponent } from './detail/cv-detail.component';
import { CVUpdateComponent } from './update/cv-update.component';
import CVResolve from './route/cv-routing-resolve.service';

const cVRoute: Routes = [
  {
    path: '',
    component: CVComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CVDetailComponent,
    resolve: {
      cV: CVResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CVUpdateComponent,
    resolve: {
      cV: CVResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CVUpdateComponent,
    resolve: {
      cV: CVResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default cVRoute;
