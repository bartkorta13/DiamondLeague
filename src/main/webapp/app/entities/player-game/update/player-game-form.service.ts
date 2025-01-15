import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPlayerGame, NewPlayerGame } from '../player-game.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPlayerGame for edit and NewPlayerGameFormGroupInput for create.
 */
type PlayerGameFormGroupInput = IPlayerGame | PartialWithRequiredKeyOf<NewPlayerGame>;

type PlayerGameFormDefaults = Pick<NewPlayerGame, 'id'>;

type PlayerGameFormGroupContent = {
  id: FormControl<IPlayerGame['id'] | NewPlayerGame['id']>;
  goals: FormControl<IPlayerGame['goals']>;
  assists: FormControl<IPlayerGame['assists']>;
  attackScore: FormControl<IPlayerGame['attackScore']>;
  defenseScore: FormControl<IPlayerGame['defenseScore']>;
  player: FormControl<IPlayerGame['player']>;
  gameTeam: FormControl<IPlayerGame['gameTeam']>;
};

export type PlayerGameFormGroup = FormGroup<PlayerGameFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PlayerGameFormService {
  createPlayerGameFormGroup(playerGame: PlayerGameFormGroupInput = { id: null }): PlayerGameFormGroup {
    const playerGameRawValue = {
      ...this.getFormDefaults(),
      ...playerGame,
    };
    return new FormGroup<PlayerGameFormGroupContent>({
      id: new FormControl(
        { value: playerGameRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      goals: new FormControl(playerGameRawValue.goals, {
        validators: [Validators.required, Validators.min(0), Validators.max(100)],
      }),
      assists: new FormControl(playerGameRawValue.assists, {
        validators: [Validators.required, Validators.min(0), Validators.max(100)],
      }),
      attackScore: new FormControl(playerGameRawValue.attackScore),
      defenseScore: new FormControl(playerGameRawValue.defenseScore),
      player: new FormControl(playerGameRawValue.player, {
        validators: [Validators.required],
      }),
      gameTeam: new FormControl(playerGameRawValue.gameTeam, {
        validators: [Validators.required],
      }),
    });
  }

  getPlayerGame(form: PlayerGameFormGroup): IPlayerGame | NewPlayerGame {
    return form.getRawValue() as IPlayerGame | NewPlayerGame;
  }

  resetForm(form: PlayerGameFormGroup, playerGame: PlayerGameFormGroupInput): void {
    const playerGameRawValue = { ...this.getFormDefaults(), ...playerGame };
    form.reset(
      {
        ...playerGameRawValue,
        id: { value: playerGameRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PlayerGameFormDefaults {
    return {
      id: null,
    };
  }
}
