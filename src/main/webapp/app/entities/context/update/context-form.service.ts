import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IContext, NewContext } from '../context.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContext for edit and NewContextFormGroupInput for create.
 */
type ContextFormGroupInput = IContext | PartialWithRequiredKeyOf<NewContext>;

type ContextFormDefaults = Pick<NewContext, 'id'>;

type ContextFormGroupContent = {
  id: FormControl<IContext['id'] | NewContext['id']>;
  name: FormControl<IContext['name']>;
  description: FormControl<IContext['description']>;
  userConfig: FormControl<IContext['userConfig']>;
};

export type ContextFormGroup = FormGroup<ContextFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContextFormService {
  createContextFormGroup(context: ContextFormGroupInput = { id: null }): ContextFormGroup {
    const contextRawValue = {
      ...this.getFormDefaults(),
      ...context,
    };
    return new FormGroup<ContextFormGroupContent>({
      id: new FormControl(
        { value: contextRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(contextRawValue.name),
      description: new FormControl(contextRawValue.description),
      userConfig: new FormControl(contextRawValue.userConfig),
    });
  }

  getContext(form: ContextFormGroup): IContext | NewContext {
    return form.getRawValue() as IContext | NewContext;
  }

  resetForm(form: ContextFormGroup, context: ContextFormGroupInput): void {
    const contextRawValue = { ...this.getFormDefaults(), ...context };
    form.reset(
      {
        ...contextRawValue,
        id: { value: contextRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ContextFormDefaults {
    return {
      id: null,
    };
  }
}
