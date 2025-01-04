import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IReference, NewReference } from '../reference.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReference for edit and NewReferenceFormGroupInput for create.
 */
type ReferenceFormGroupInput = IReference | PartialWithRequiredKeyOf<NewReference>;

type ReferenceFormDefaults = Pick<NewReference, 'id'>;

type ReferenceFormGroupContent = {
  id: FormControl<IReference['id'] | NewReference['id']>;
  title: FormControl<IReference['title']>;
  content: FormControl<IReference['content']>;
  lastmodified: FormControl<IReference['lastmodified']>;
  userConfig: FormControl<IReference['userConfig']>;
};

export type ReferenceFormGroup = FormGroup<ReferenceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReferenceFormService {
  createReferenceFormGroup(reference: ReferenceFormGroupInput = { id: null }): ReferenceFormGroup {
    const referenceRawValue = {
      ...this.getFormDefaults(),
      ...reference,
    };
    return new FormGroup<ReferenceFormGroupContent>({
      id: new FormControl(
        { value: referenceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(referenceRawValue.title),
      content: new FormControl(referenceRawValue.content),
      lastmodified: new FormControl(referenceRawValue.lastmodified),
      userConfig: new FormControl(referenceRawValue.userConfig),
    });
  }

  getReference(form: ReferenceFormGroup): IReference | NewReference {
    return form.getRawValue() as IReference | NewReference;
  }

  resetForm(form: ReferenceFormGroup, reference: ReferenceFormGroupInput): void {
    const referenceRawValue = { ...this.getFormDefaults(), ...reference };
    form.reset(
      {
        ...referenceRawValue,
        id: { value: referenceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReferenceFormDefaults {
    return {
      id: null,
    };
  }
}
