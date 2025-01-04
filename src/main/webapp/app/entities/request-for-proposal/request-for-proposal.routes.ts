import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { RequestForProposalComponent } from './request-for-proposal.component';

const RFPRoutes: Routes = [
  {
    path: '',
    component: RequestForProposalComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default RFPRoutes;
