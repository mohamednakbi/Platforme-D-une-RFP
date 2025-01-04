import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ResultatsRFPComponent } from './resultats-rfp.component';

const RFPRoutes: Routes = [
  {
    path: '',
    component: ResultatsRFPComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default RFPRoutes;
