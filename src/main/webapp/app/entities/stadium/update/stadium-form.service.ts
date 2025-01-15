import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IStadium, NewStadium } from '../stadium.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStadium for edit and NewStadiumFormGroupInput for create.
 */
type StadiumFormGroupInput = IStadium | PartialWithRequiredKeyOf<NewStadium>;

type StadiumFormDefaults = Pick<NewStadium, 'id'>;

type StadiumFormGroupContent = {
  id: FormControl<IStadium['id'] | NewStadium['id']>;
  name: FormControl<IStadium['name']>;
  imagePath: FormControl<IStadium['imagePath']>;
};

export type StadiumFormGroup = FormGroup<StadiumFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StadiumFormService {
  createStadiumFormGroup(stadium: StadiumFormGroupInput = { id: null }): StadiumFormGroup {
    const stadiumRawValue = {
      ...this.getFormDefaults(),
      ...stadium,
    };
    return new FormGroup<StadiumFormGroupContent>({
      id: new FormControl(
        { value: stadiumRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(stadiumRawValue.name, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(50)],
      }),
      imagePath: new FormControl(stadiumRawValue.imagePath),
    });
  }

  getStadium(form: StadiumFormGroup): IStadium | NewStadium {
    return form.getRawValue() as IStadium | NewStadium;
  }

  resetForm(form: StadiumFormGroup, stadium: StadiumFormGroupInput): void {
    const stadiumRawValue = { ...this.getFormDefaults(), ...stadium };
    form.reset(
      {
        ...stadiumRawValue,
        id: { value: stadiumRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): StadiumFormDefaults {
    return {
      id: null,
    };
  }
}
