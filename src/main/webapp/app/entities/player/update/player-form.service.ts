import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPlayer, NewPlayer } from '../player.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPlayer for edit and NewPlayerFormGroupInput for create.
 */
type PlayerFormGroupInput = IPlayer | PartialWithRequiredKeyOf<NewPlayer>;

type PlayerFormDefaults = Pick<NewPlayer, 'id'>;

type PlayerFormGroupContent = {
  id: FormControl<IPlayer['id'] | NewPlayer['id']>;
  firstName: FormControl<IPlayer['firstName']>;
  lastName: FormControl<IPlayer['lastName']>;
  nickname: FormControl<IPlayer['nickname']>;
  height: FormControl<IPlayer['height']>;
  yearOfBirth: FormControl<IPlayer['yearOfBirth']>;
  preferredPosition: FormControl<IPlayer['preferredPosition']>;
  appUser: FormControl<IPlayer['appUser']>;
  favouriteClub: FormControl<IPlayer['favouriteClub']>;
};

export type PlayerFormGroup = FormGroup<PlayerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PlayerFormService {
  createPlayerFormGroup(player: PlayerFormGroupInput = { id: null }): PlayerFormGroup {
    const playerRawValue = {
      ...this.getFormDefaults(),
      ...player,
    };
    return new FormGroup<PlayerFormGroupContent>({
      id: new FormControl(
        { value: playerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      firstName: new FormControl(playerRawValue.firstName, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(40)],
      }),
      lastName: new FormControl(playerRawValue.lastName, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(40)],
      }),
      nickname: new FormControl(playerRawValue.nickname, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(40)],
      }),
      height: new FormControl(playerRawValue.height, {
        validators: [Validators.min(100), Validators.max(250)],
      }),
      yearOfBirth: new FormControl(playerRawValue.yearOfBirth, {
        validators: [Validators.min(1900), Validators.max(2050)],
      }),
      preferredPosition: new FormControl(playerRawValue.preferredPosition, {
        validators: [Validators.required],
      }),
      appUser: new FormControl(playerRawValue.appUser),
      favouriteClub: new FormControl(playerRawValue.favouriteClub),
    });
  }

  getPlayer(form: PlayerFormGroup): IPlayer | NewPlayer {
    return form.getRawValue() as IPlayer | NewPlayer;
  }

  resetForm(form: PlayerFormGroup, player: PlayerFormGroupInput): void {
    const playerRawValue = { ...this.getFormDefaults(), ...player };
    form.reset(
      {
        ...playerRawValue,
        id: { value: playerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PlayerFormDefaults {
    return {
      id: null,
    };
  }
}
