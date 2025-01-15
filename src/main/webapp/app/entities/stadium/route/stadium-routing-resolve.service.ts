import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStadium } from '../stadium.model';
import { StadiumService } from '../service/stadium.service';

const stadiumResolve = (route: ActivatedRouteSnapshot): Observable<null | IStadium> => {
  const id = route.params.id;
  if (id) {
    return inject(StadiumService)
      .find(id)
      .pipe(
        mergeMap((stadium: HttpResponse<IStadium>) => {
          if (stadium.body) {
            return of(stadium.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default stadiumResolve;
