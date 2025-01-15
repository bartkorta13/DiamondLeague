import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStadium, NewStadium } from '../stadium.model';

export type PartialUpdateStadium = Partial<IStadium> & Pick<IStadium, 'id'>;

export type EntityResponseType = HttpResponse<IStadium>;
export type EntityArrayResponseType = HttpResponse<IStadium[]>;

@Injectable({ providedIn: 'root' })
export class StadiumService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/stadiums');

  create(stadium: NewStadium): Observable<EntityResponseType> {
    return this.http.post<IStadium>(this.resourceUrl, stadium, { observe: 'response' });
  }

  update(stadium: IStadium): Observable<EntityResponseType> {
    return this.http.put<IStadium>(`${this.resourceUrl}/${this.getStadiumIdentifier(stadium)}`, stadium, { observe: 'response' });
  }

  partialUpdate(stadium: PartialUpdateStadium): Observable<EntityResponseType> {
    return this.http.patch<IStadium>(`${this.resourceUrl}/${this.getStadiumIdentifier(stadium)}`, stadium, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStadium>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStadium[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStadiumIdentifier(stadium: Pick<IStadium, 'id'>): number {
    return stadium.id;
  }

  compareStadium(o1: Pick<IStadium, 'id'> | null, o2: Pick<IStadium, 'id'> | null): boolean {
    return o1 && o2 ? this.getStadiumIdentifier(o1) === this.getStadiumIdentifier(o2) : o1 === o2;
  }

  addStadiumToCollectionIfMissing<Type extends Pick<IStadium, 'id'>>(
    stadiumCollection: Type[],
    ...stadiumsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const stadiums: Type[] = stadiumsToCheck.filter(isPresent);
    if (stadiums.length > 0) {
      const stadiumCollectionIdentifiers = stadiumCollection.map(stadiumItem => this.getStadiumIdentifier(stadiumItem));
      const stadiumsToAdd = stadiums.filter(stadiumItem => {
        const stadiumIdentifier = this.getStadiumIdentifier(stadiumItem);
        if (stadiumCollectionIdentifiers.includes(stadiumIdentifier)) {
          return false;
        }
        stadiumCollectionIdentifiers.push(stadiumIdentifier);
        return true;
      });
      return [...stadiumsToAdd, ...stadiumCollection];
    }
    return stadiumCollection;
  }
}
