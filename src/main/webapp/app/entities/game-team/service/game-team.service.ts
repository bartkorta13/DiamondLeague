import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGameTeam, NewGameTeam } from '../game-team.model';

export type PartialUpdateGameTeam = Partial<IGameTeam> & Pick<IGameTeam, 'id'>;

export type EntityResponseType = HttpResponse<IGameTeam>;
export type EntityArrayResponseType = HttpResponse<IGameTeam[]>;

@Injectable({ providedIn: 'root' })
export class GameTeamService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/game-teams');

  create(gameTeam: NewGameTeam): Observable<EntityResponseType> {
    return this.http.post<IGameTeam>(this.resourceUrl, gameTeam, { observe: 'response' });
  }

  update(gameTeam: IGameTeam): Observable<EntityResponseType> {
    return this.http.put<IGameTeam>(`${this.resourceUrl}/${this.getGameTeamIdentifier(gameTeam)}`, gameTeam, { observe: 'response' });
  }

  partialUpdate(gameTeam: PartialUpdateGameTeam): Observable<EntityResponseType> {
    return this.http.patch<IGameTeam>(`${this.resourceUrl}/${this.getGameTeamIdentifier(gameTeam)}`, gameTeam, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGameTeam>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGameTeam[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGameTeamIdentifier(gameTeam: Pick<IGameTeam, 'id'>): number {
    return gameTeam.id;
  }

  compareGameTeam(o1: Pick<IGameTeam, 'id'> | null, o2: Pick<IGameTeam, 'id'> | null): boolean {
    return o1 && o2 ? this.getGameTeamIdentifier(o1) === this.getGameTeamIdentifier(o2) : o1 === o2;
  }

  addGameTeamToCollectionIfMissing<Type extends Pick<IGameTeam, 'id'>>(
    gameTeamCollection: Type[],
    ...gameTeamsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const gameTeams: Type[] = gameTeamsToCheck.filter(isPresent);
    if (gameTeams.length > 0) {
      const gameTeamCollectionIdentifiers = gameTeamCollection.map(gameTeamItem => this.getGameTeamIdentifier(gameTeamItem));
      const gameTeamsToAdd = gameTeams.filter(gameTeamItem => {
        const gameTeamIdentifier = this.getGameTeamIdentifier(gameTeamItem);
        if (gameTeamCollectionIdentifiers.includes(gameTeamIdentifier)) {
          return false;
        }
        gameTeamCollectionIdentifiers.push(gameTeamIdentifier);
        return true;
      });
      return [...gameTeamsToAdd, ...gameTeamCollection];
    }
    return gameTeamCollection;
  }
}
