import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { RoleComponent } from './list/role.component';
import { RoleDetailComponent } from './detail/role-detail.component';
import { RoleUpdateComponent } from './update/role-update.component';
import RoleResolve from './route/role-routing-resolve.service';

const roleRoute: Routes = [
  {
    path: '',
    component: RoleComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RoleDetailComponent,
    resolve: {
      role: RoleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RoleUpdateComponent,
    resolve: {
      role: RoleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RoleUpdateComponent,
    resolve: {
      role: RoleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default roleRoute;
