import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { UserConfigComponent } from './list/user-config.component';
import { UserConfigDetailComponent } from './detail/user-config-detail.component';
import { UserConfigUpdateComponent } from './update/user-config-update.component';
import UserConfigResolve from './route/user-config-routing-resolve.service';

const userConfigRoute: Routes = [
  {
    path: '',
    component: UserConfigComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserConfigDetailComponent,
    resolve: {
      userConfig: UserConfigResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserConfigUpdateComponent,
    resolve: {
      userConfig: UserConfigResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserConfigUpdateComponent,
    resolve: {
      userConfig: UserConfigResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default userConfigRoute;
