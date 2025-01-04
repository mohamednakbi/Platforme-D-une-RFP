import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUserConfig, NewUserConfig } from '../user-config.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserConfig for edit and NewUserConfigFormGroupInput for create.
 */
type UserConfigFormGroupInput = IUserConfig | PartialWithRequiredKeyOf<NewUserConfig>;

type UserConfigFormDefaults = Pick<NewUserConfig, 'id' | 'technologys'>;

type UserConfigFormGroupContent = {
  id: FormControl<IUserConfig['id'] | NewUserConfig['id']>;
  userId: FormControl<IUserConfig['userId']>;
  email: FormControl<IUserConfig['email']>;
  firstname: FormControl<IUserConfig['firstname']>;
  lastname: FormControl<IUserConfig['lastname']>;
  username: FormControl<IUserConfig['username']>;
  password: FormControl<IUserConfig['password']>;
  role: FormControl<IUserConfig['role']>;
  technologys: FormControl<IUserConfig['technologys']>;
};

export type UserConfigFormGroup = FormGroup<UserConfigFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserConfigFormService {
  createUserConfigFormGroup(userConfig: UserConfigFormGroupInput = { id: null }): UserConfigFormGroup {
    const userConfigRawValue = {
      ...this.getFormDefaults(),
      ...userConfig,
    };
    return new FormGroup<UserConfigFormGroupContent>({
      id: new FormControl(
        { value: userConfigRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      userId: new FormControl(userConfigRawValue.userId),
      email: new FormControl(userConfigRawValue.email),
      firstname: new FormControl(userConfigRawValue.firstname),
      lastname: new FormControl(userConfigRawValue.lastname),
      username: new FormControl(userConfigRawValue.username),
      password: new FormControl(userConfigRawValue.password),
      role: new FormControl(userConfigRawValue.role),
      technologys: new FormControl(userConfigRawValue.technologys ?? []),
    });
  }

  getUserConfig(form: UserConfigFormGroup): IUserConfig | NewUserConfig {
    return form.getRawValue() as IUserConfig | NewUserConfig;
  }

  resetForm(form: UserConfigFormGroup, userConfig: UserConfigFormGroupInput): void {
    const userConfigRawValue = { ...this.getFormDefaults(), ...userConfig };
    form.reset(
      {
        ...userConfigRawValue,
        id: { value: userConfigRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserConfigFormDefaults {
    return {
      id: null,
      technologys: [],
    };
  }
}
