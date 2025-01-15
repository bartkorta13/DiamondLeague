import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import StadiumResolve from './route/stadium-routing-resolve.service';

const stadiumRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/stadium.component').then(m => m.StadiumComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/stadium-detail.component').then(m => m.StadiumDetailComponent),
    resolve: {
      stadium: StadiumResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/stadium-update.component').then(m => m.StadiumUpdateComponent),
    resolve: {
      stadium: StadiumResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/stadium-update.component').then(m => m.StadiumUpdateComponent),
    resolve: {
      stadium: StadiumResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default stadiumRoute;
