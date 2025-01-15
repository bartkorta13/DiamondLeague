import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IStadium } from 'app/entities/stadium/stadium.model';
import { StadiumService } from 'app/entities/stadium/service/stadium.service';
import { IGame } from '../game.model';
import { GameService } from '../service/game.service';
import { GameFormGroup, GameFormService } from './game-form.service';

@Component({
  selector: 'jhi-game-update',
  templateUrl: './game-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class GameUpdateComponent implements OnInit {
  isSaving = false;
  game: IGame | null = null;

  stadiumsSharedCollection: IStadium[] = [];

  protected gameService = inject(GameService);
  protected gameFormService = inject(GameFormService);
  protected stadiumService = inject(StadiumService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: GameFormGroup = this.gameFormService.createGameFormGroup();

  compareStadium = (o1: IStadium | null, o2: IStadium | null): boolean => this.stadiumService.compareStadium(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ game }) => {
      this.game = game;
      if (game) {
        this.updateForm(game);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const game = this.gameFormService.getGame(this.editForm);
    if (game.id !== null) {
      this.subscribeToSaveResponse(this.gameService.update(game));
    } else {
      this.subscribeToSaveResponse(this.gameService.create(game));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGame>>): void {
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

  protected updateForm(game: IGame): void {
    this.game = game;
    this.gameFormService.resetForm(this.editForm, game);

    this.stadiumsSharedCollection = this.stadiumService.addStadiumToCollectionIfMissing<IStadium>(
      this.stadiumsSharedCollection,
      game.stadium,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.stadiumService
      .query()
      .pipe(map((res: HttpResponse<IStadium[]>) => res.body ?? []))
      .pipe(map((stadiums: IStadium[]) => this.stadiumService.addStadiumToCollectionIfMissing<IStadium>(stadiums, this.game?.stadium)))
      .subscribe((stadiums: IStadium[]) => (this.stadiumsSharedCollection = stadiums));
  }
}
