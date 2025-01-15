import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';
import { IGameTeam } from 'app/entities/game-team/game-team.model';
import { GameTeamService } from 'app/entities/game-team/service/game-team.service';
import { PlayerGameService } from '../service/player-game.service';
import { IPlayerGame } from '../player-game.model';
import { PlayerGameFormGroup, PlayerGameFormService } from './player-game-form.service';

@Component({
  selector: 'jhi-player-game-update',
  templateUrl: './player-game-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PlayerGameUpdateComponent implements OnInit {
  isSaving = false;
  playerGame: IPlayerGame | null = null;

  playersSharedCollection: IPlayer[] = [];
  gameTeamsSharedCollection: IGameTeam[] = [];

  protected playerGameService = inject(PlayerGameService);
  protected playerGameFormService = inject(PlayerGameFormService);
  protected playerService = inject(PlayerService);
  protected gameTeamService = inject(GameTeamService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PlayerGameFormGroup = this.playerGameFormService.createPlayerGameFormGroup();

  comparePlayer = (o1: IPlayer | null, o2: IPlayer | null): boolean => this.playerService.comparePlayer(o1, o2);

  compareGameTeam = (o1: IGameTeam | null, o2: IGameTeam | null): boolean => this.gameTeamService.compareGameTeam(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playerGame }) => {
      this.playerGame = playerGame;
      if (playerGame) {
        this.updateForm(playerGame);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const playerGame = this.playerGameFormService.getPlayerGame(this.editForm);
    if (playerGame.id !== null) {
      this.subscribeToSaveResponse(this.playerGameService.update(playerGame));
    } else {
      this.subscribeToSaveResponse(this.playerGameService.create(playerGame));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlayerGame>>): void {
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

  protected updateForm(playerGame: IPlayerGame): void {
    this.playerGame = playerGame;
    this.playerGameFormService.resetForm(this.editForm, playerGame);

    this.playersSharedCollection = this.playerService.addPlayerToCollectionIfMissing<IPlayer>(
      this.playersSharedCollection,
      playerGame.player,
    );
    this.gameTeamsSharedCollection = this.gameTeamService.addGameTeamToCollectionIfMissing<IGameTeam>(
      this.gameTeamsSharedCollection,
      playerGame.gameTeam,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.playerService
      .query()
      .pipe(map((res: HttpResponse<IPlayer[]>) => res.body ?? []))
      .pipe(map((players: IPlayer[]) => this.playerService.addPlayerToCollectionIfMissing<IPlayer>(players, this.playerGame?.player)))
      .subscribe((players: IPlayer[]) => (this.playersSharedCollection = players));

    this.gameTeamService
      .query()
      .pipe(map((res: HttpResponse<IGameTeam[]>) => res.body ?? []))
      .pipe(
        map((gameTeams: IGameTeam[]) =>
          this.gameTeamService.addGameTeamToCollectionIfMissing<IGameTeam>(gameTeams, this.playerGame?.gameTeam),
        ),
      )
      .subscribe((gameTeams: IGameTeam[]) => (this.gameTeamsSharedCollection = gameTeams));
  }
}
