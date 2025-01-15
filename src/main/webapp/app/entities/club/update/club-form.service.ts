import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IClub, NewClub } from '../club.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IClub for edit and NewClubFormGroupInput for create.
 */
type ClubFormGroupInput = IClub | PartialWithRequiredKeyOf<NewClub>;

type ClubFormDefaults = Pick<NewClub, 'id'>;

type ClubFormGroupContent = {
  id: FormControl<IClub['id'] | NewClub['id']>;
  name: FormControl<IClub['name']>;
  logoPath: FormControl<IClub['logoPath']>;
};

export type ClubFormGroup = FormGroup<ClubFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClubFormService {
  createClubFormGroup(club: ClubFormGroupInput = { id: null }): ClubFormGroup {
    const clubRawValue = {
      ...this.getFormDefaults(),
      ...club,
    };
    return new FormGroup<ClubFormGroupContent>({
      id: new FormControl(
        { value: clubRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(clubRawValue.name, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(50)],
      }),
      logoPath: new FormControl(clubRawValue.logoPath),
    });
  }

  getClub(form: ClubFormGroup): IClub | NewClub {
    return form.getRawValue() as IClub | NewClub;
  }

  resetForm(form: ClubFormGroup, club: ClubFormGroupInput): void {
    const clubRawValue = { ...this.getFormDefaults(), ...club };
    form.reset(
      {
        ...clubRawValue,
        id: { value: clubRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ClubFormDefaults {
    return {
      id: null,
    };
  }
}
