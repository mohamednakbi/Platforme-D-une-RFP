import { Routes } from '@angular/router';

import { Authority } from 'app/config/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { errorRoute } from './layouts/error/error.route';

import HomeComponent from './home/home.component';
import NavbarComponent from './layouts/navbar/navbar.component';
import { SideNavbarComponent } from './layouts/side-navbar/side-navbar.component';
import { SplashScreenComponent } from './entities/splash-screen/splash-screen.component';
import { ResultatsRFPComponent } from './entities/resultats-rfp/resultats-rfp.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    title: 'home.title',
  },
  { path: 'ResultatRFP', component: SplashScreenComponent },
  { path: 'ResultatRfp', component: ResultatsRFPComponent },
  {
    path: '',
    component: NavbarComponent,
    outlet: 'navbar',
  },
  {
    path: '',
    component: SideNavbarComponent,
    outlet: 'sidebar',
  },
  {
    path: 'admin',
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
    loadChildren: () => import('./admin/admin.routes'),
  },
  {
    path: '',
    loadChildren: () => import(`./entities/entity.routes`),
  },
  ...errorRoute,
];

export default routes;
