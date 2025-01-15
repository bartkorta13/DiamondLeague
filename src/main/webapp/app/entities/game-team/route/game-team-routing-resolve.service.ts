import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGameTeam } from '../game-team.model';
import { GameTeamService } from '../service/game-team.service';

const gameTeamResolve = (route: ActivatedRouteSnapshot): Observable<null | IGameTeam> => {
  const id = route.params.id;
  if (id) {
    return inject(GameTeamService)
      .find(id)
      .pipe(
        mergeMap((gameTeam: HttpResponse<IGameTeam>) => {
          if (gameTeam.body) {
            return of(gameTeam.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default gameTeamResolve;
