import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRating, NewRating } from '../rating.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRating for edit and NewRatingFormGroupInput for create.
 */
type RatingFormGroupInput = IRating | PartialWithRequiredKeyOf<NewRating>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRating | NewRating> = Omit<T, 'date'> & {
  date?: string | null;
};

type RatingFormRawValue = FormValueOf<IRating>;

type NewRatingFormRawValue = FormValueOf<NewRating>;

type RatingFormDefaults = Pick<NewRating, 'id' | 'date'>;

type RatingFormGroupContent = {
  id: FormControl<RatingFormRawValue['id'] | NewRating['id']>;
  date: FormControl<RatingFormRawValue['date']>;
  attack: FormControl<RatingFormRawValue['attack']>;
  defense: FormControl<RatingFormRawValue['defense']>;
  engagement: FormControl<RatingFormRawValue['engagement']>;
  overall: FormControl<RatingFormRawValue['overall']>;
  player: FormControl<RatingFormRawValue['player']>;
};

export type RatingFormGroup = FormGroup<RatingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RatingFormService {
  createRatingFormGroup(rating: RatingFormGroupInput = { id: null }): RatingFormGroup {
    const ratingRawValue = this.convertRatingToRatingRawValue({
      ...this.getFormDefaults(),
      ...rating,
    });
    return new FormGroup<RatingFormGroupContent>({
      id: new FormControl(
        { value: ratingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      date: new FormControl(ratingRawValue.date, {
        validators: [Validators.required],
      }),
      attack: new FormControl(ratingRawValue.attack, {
        validators: [Validators.required, Validators.min(1), Validators.max(99)],
      }),
      defense: new FormControl(ratingRawValue.defense, {
        validators: [Validators.required, Validators.min(1), Validators.max(99)],
      }),
      engagement: new FormControl(ratingRawValue.engagement, {
        validators: [Validators.required, Validators.min(1), Validators.max(99)],
      }),
      overall: new FormControl(ratingRawValue.overall, {
        validators: [Validators.required, Validators.min(70), Validators.max(99)],
      }),
      player: new FormControl(ratingRawValue.player, {
        validators: [Validators.required],
      }),
    });
  }

  getRating(form: RatingFormGroup): IRating | NewRating {
    return this.convertRatingRawValueToRating(form.getRawValue() as RatingFormRawValue | NewRatingFormRawValue);
  }

  resetForm(form: RatingFormGroup, rating: RatingFormGroupInput): void {
    const ratingRawValue = this.convertRatingToRatingRawValue({ ...this.getFormDefaults(), ...rating });
    form.reset(
      {
        ...ratingRawValue,
        id: { value: ratingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RatingFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      date: currentTime,
    };
  }

  private convertRatingRawValueToRating(rawRating: RatingFormRawValue | NewRatingFormRawValue): IRating | NewRating {
    return {
      ...rawRating,
      date: dayjs(rawRating.date, DATE_TIME_FORMAT),
    };
  }

  private convertRatingToRatingRawValue(
    rating: IRating | (Partial<NewRating> & RatingFormDefaults),
  ): RatingFormRawValue | PartialWithRequiredKeyOf<NewRatingFormRawValue> {
    return {
      ...rating,
      date: rating.date ? rating.date.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
