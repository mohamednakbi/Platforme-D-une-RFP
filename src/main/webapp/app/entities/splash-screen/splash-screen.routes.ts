import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SplashScreenComponent } from './splash-screen.component';

const RFPRoutes: Routes = [
  {
    path: '',
    component: SplashScreenComponent,

    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default RFPRoutes;
