import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PlayerResolve from './route/player-routing-resolve.service';

const playerRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/player.component').then(m => m.PlayerComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/player-detail.component').then(m => m.PlayerDetailComponent),
    resolve: {
      player: PlayerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/player-update.component').then(m => m.PlayerUpdateComponent),
    resolve: {
      player: PlayerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/player-update.component').then(m => m.PlayerUpdateComponent),
    resolve: {
      player: PlayerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default playerRoute;
