import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';
import { IRating } from '../rating.model';
import { RatingService } from '../service/rating.service';
import { RatingFormGroup, RatingFormService } from './rating-form.service';

@Component({
  selector: 'jhi-rating-update',
  templateUrl: './rating-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RatingUpdateComponent implements OnInit {
  isSaving = false;
  rating: IRating | null = null;

  playersSharedCollection: IPlayer[] = [];

  protected ratingService = inject(RatingService);
  protected ratingFormService = inject(RatingFormService);
  protected playerService = inject(PlayerService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RatingFormGroup = this.ratingFormService.createRatingFormGroup();

  comparePlayer = (o1: IPlayer | null, o2: IPlayer | null): boolean => this.playerService.comparePlayer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rating }) => {
      this.rating = rating;
      if (rating) {
        this.updateForm(rating);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rating = this.ratingFormService.getRating(this.editForm);
    if (rating.id !== null) {
      this.subscribeToSaveResponse(this.ratingService.update(rating));
    } else {
      this.subscribeToSaveResponse(this.ratingService.create(rating));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRating>>): void {
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

  protected updateForm(rating: IRating): void {
    this.rating = rating;
    this.ratingFormService.resetForm(this.editForm, rating);

    this.playersSharedCollection = this.playerService.addPlayerToCollectionIfMissing<IPlayer>(this.playersSharedCollection, rating.player);
  }

  protected loadRelationshipsOptions(): void {
    this.playerService
      .query()
      .pipe(map((res: HttpResponse<IPlayer[]>) => res.body ?? []))
      .pipe(map((players: IPlayer[]) => this.playerService.addPlayerToCollectionIfMissing<IPlayer>(players, this.rating?.player)))
      .subscribe((players: IPlayer[]) => (this.playersSharedCollection = players));
  }
}
