import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlayer } from '../player.model';
import { PlayerService } from '../service/player.service';

const playerResolve = (route: ActivatedRouteSnapshot): Observable<null | IPlayer> => {
  const id = route.params.id;
  if (id) {
    return inject(PlayerService)
      .find(id)
      .pipe(
        mergeMap((player: HttpResponse<IPlayer>) => {
          if (player.body) {
            return of(player.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default playerResolve;
