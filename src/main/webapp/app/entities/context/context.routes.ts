import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ContextComponent } from './list/context.component';
import { ContextDetailComponent } from './detail/context-detail.component';
import { ContextUpdateComponent } from './update/context-update.component';
import ContextResolve from './route/context-routing-resolve.service';

const contextRoute: Routes = [
  {
    path: '',
    component: ContextComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContextDetailComponent,
    resolve: {
      context: ContextResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContextUpdateComponent,
    resolve: {
      context: ContextResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContextUpdateComponent,
    resolve: {
      context: ContextResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default contextRoute;
