import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import GameTeamResolve from './route/game-team-routing-resolve.service';

const gameTeamRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/game-team.component').then(m => m.GameTeamComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/game-team-detail.component').then(m => m.GameTeamDetailComponent),
    resolve: {
      gameTeam: GameTeamResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/game-team-update.component').then(m => m.GameTeamUpdateComponent),
    resolve: {
      gameTeam: GameTeamResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/game-team-update.component').then(m => m.GameTeamUpdateComponent),
    resolve: {
      gameTeam: GameTeamResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default gameTeamRoute;
