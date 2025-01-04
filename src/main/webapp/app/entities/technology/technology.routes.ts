import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TechnologyComponent } from './list/technology.component';
import { TechnologyDetailComponent } from './detail/technology-detail.component';
import { TechnologyUpdateComponent } from './update/technology-update.component';
import TechnologyResolve from './route/technology-routing-resolve.service';

const technologyRoute: Routes = [
  {
    path: '',
    component: TechnologyComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TechnologyDetailComponent,
    resolve: {
      technology: TechnologyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TechnologyUpdateComponent,
    resolve: {
      technology: TechnologyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TechnologyUpdateComponent,
    resolve: {
      technology: TechnologyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default technologyRoute;
