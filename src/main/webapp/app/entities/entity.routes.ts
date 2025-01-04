import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'technology',
    data: { pageTitle: 'rfpApp.technology.home.title' },
    loadChildren: () => import('./technology/technology.routes'),
  },
  {
    path: 'RequestForProposal',
    data: { pageTitle: 'rfpApp.RequestForProposal.home.title' },
    loadChildren: () => import('./request-for-proposal/request-for-proposal.routes'),
  },
  {
    path: 'ResultatRfp',
    data: { pageTitle: 'rfpApp.ResultatRfp.home.title' },
    loadChildren: () => import('./resultats-rfp/resultats-rfp.routes'),
  },
  {
    path: 'splashScreen',
    data: { pageTitle: 'rfpApp.splash-screen.home.title' },
    loadChildren: () => import('./splash-screen/splash-screen.routes'),
  },
  {
    path: 'role',
    data: { pageTitle: 'rfpApp.role.home.title' },
    loadChildren: () => import('./role/role.routes'),
  },
  {
    path: 'cv',
    data: { pageTitle: 'rfpApp.cV.home.title' },
    loadChildren: () => import('./cv/cv.routes'),
  },
  {
    path: 'document',
    data: { pageTitle: 'rfpApp.document.home.title' },
    loadChildren: () => import('./document/document.routes'),
  },
  {
    path: 'context',
    data: { pageTitle: 'rfpApp.context.home.title' },
    loadChildren: () => import('./context/context.routes'),
  },
  {
    path: 'user-config',
    data: { pageTitle: 'rfpApp.userConfig.home.title' },
    loadChildren: () => import('./user-config/user-config.routes'),
  },
  {
    path: 'reference',
    data: { pageTitle: 'rfpApp.reference.home.title' },
    loadChildren: () => import('./reference/reference.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
