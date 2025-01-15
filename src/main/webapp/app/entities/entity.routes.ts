import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'diamondLeagueApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'player',
    data: { pageTitle: 'diamondLeagueApp.player.home.title' },
    loadChildren: () => import('./player/player.routes'),
  },
  {
    path: 'club',
    data: { pageTitle: 'diamondLeagueApp.club.home.title' },
    loadChildren: () => import('./club/club.routes'),
  },
  {
    path: 'rating',
    data: { pageTitle: 'diamondLeagueApp.rating.home.title' },
    loadChildren: () => import('./rating/rating.routes'),
  },
  {
    path: 'game',
    data: { pageTitle: 'diamondLeagueApp.game.home.title' },
    loadChildren: () => import('./game/game.routes'),
  },
  {
    path: 'game-team',
    data: { pageTitle: 'diamondLeagueApp.gameTeam.home.title' },
    loadChildren: () => import('./game-team/game-team.routes'),
  },
  {
    path: 'player-game',
    data: { pageTitle: 'diamondLeagueApp.playerGame.home.title' },
    loadChildren: () => import('./player-game/player-game.routes'),
  },
  {
    path: 'stadium',
    data: { pageTitle: 'diamondLeagueApp.stadium.home.title' },
    loadChildren: () => import('./stadium/stadium.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
