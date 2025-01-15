import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IGameTeam, NewGameTeam } from '../game-team.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGameTeam for edit and NewGameTeamFormGroupInput for create.
 */
type GameTeamFormGroupInput = IGameTeam | PartialWithRequiredKeyOf<NewGameTeam>;

type GameTeamFormDefaults = Pick<NewGameTeam, 'id'>;

type GameTeamFormGroupContent = {
  id: FormControl<IGameTeam['id'] | NewGameTeam['id']>;
  goals: FormControl<IGameTeam['goals']>;
  captain: FormControl<IGameTeam['captain']>;
  game: FormControl<IGameTeam['game']>;
};

export type GameTeamFormGroup = FormGroup<GameTeamFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GameTeamFormService {
  createGameTeamFormGroup(gameTeam: GameTeamFormGroupInput = { id: null }): GameTeamFormGroup {
    const gameTeamRawValue = {
      ...this.getFormDefaults(),
      ...gameTeam,
    };
    return new FormGroup<GameTeamFormGroupContent>({
      id: new FormControl(
        { value: gameTeamRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      goals: new FormControl(gameTeamRawValue.goals, {
        validators: [Validators.min(0), Validators.max(100)],
      }),
      captain: new FormControl(gameTeamRawValue.captain, {
        validators: [Validators.required],
      }),
      game: new FormControl(gameTeamRawValue.game, {
        validators: [Validators.required],
      }),
    });
  }

  getGameTeam(form: GameTeamFormGroup): IGameTeam | NewGameTeam {
    return form.getRawValue() as IGameTeam | NewGameTeam;
  }

  resetForm(form: GameTeamFormGroup, gameTeam: GameTeamFormGroupInput): void {
    const gameTeamRawValue = { ...this.getFormDefaults(), ...gameTeam };
    form.reset(
      {
        ...gameTeamRawValue,
        id: { value: gameTeamRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): GameTeamFormDefaults {
    return {
      id: null,
    };
  }
}
