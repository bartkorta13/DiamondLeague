import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';
import { IGame } from 'app/entities/game/game.model';
import { GameService } from 'app/entities/game/service/game.service';
import { GameTeamService } from '../service/game-team.service';
import { IGameTeam } from '../game-team.model';
import { GameTeamFormGroup, GameTeamFormService } from './game-team-form.service';

@Component({
  selector: 'jhi-game-team-update',
  templateUrl: './game-team-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class GameTeamUpdateComponent implements OnInit {
  isSaving = false;
  gameTeam: IGameTeam | null = null;

  playersSharedCollection: IPlayer[] = [];
  gamesSharedCollection: IGame[] = [];

  protected gameTeamService = inject(GameTeamService);
  protected gameTeamFormService = inject(GameTeamFormService);
  protected playerService = inject(PlayerService);
  protected gameService = inject(GameService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: GameTeamFormGroup = this.gameTeamFormService.createGameTeamFormGroup();

  comparePlayer = (o1: IPlayer | null, o2: IPlayer | null): boolean => this.playerService.comparePlayer(o1, o2);

  compareGame = (o1: IGame | null, o2: IGame | null): boolean => this.gameService.compareGame(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gameTeam }) => {
      this.gameTeam = gameTeam;
      if (gameTeam) {
        this.updateForm(gameTeam);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const gameTeam = this.gameTeamFormService.getGameTeam(this.editForm);
    if (gameTeam.id !== null) {
      this.subscribeToSaveResponse(this.gameTeamService.update(gameTeam));
    } else {
      this.subscribeToSaveResponse(this.gameTeamService.create(gameTeam));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGameTeam>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(gameTeam: IGameTeam): void {
    this.gameTeam = gameTeam;
    this.gameTeamFormService.resetForm(this.editForm, gameTeam);

    this.playersSharedCollection = this.playerService.addPlayerToCollectionIfMissing<IPlayer>(
      this.playersSharedCollection,
      gameTeam.captain,
    );
    this.gamesSharedCollection = this.gameService.addGameToCollectionIfMissing<IGame>(this.gamesSharedCollection, gameTeam.game);
  }

  protected loadRelationshipsOptions(): void {
    this.playerService
      .query()
      .pipe(map((res: HttpResponse<IPlayer[]>) => res.body ?? []))
      .pipe(map((players: IPlayer[]) => this.playerService.addPlayerToCollectionIfMissing<IPlayer>(players, this.gameTeam?.captain)))
      .subscribe((players: IPlayer[]) => (this.playersSharedCollection = players));

    this.gameService
      .query()
      .pipe(map((res: HttpResponse<IGame[]>) => res.body ?? []))
      .pipe(map((games: IGame[]) => this.gameService.addGameToCollectionIfMissing<IGame>(games, this.gameTeam?.game)))
      .subscribe((games: IGame[]) => (this.gamesSharedCollection = games));
  }
}
