import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUserConfig } from 'app/entities/user-config/user-config.model';
import { UserConfigService } from 'app/entities/user-config/service/user-config.service';
import { IReference } from '../reference.model';
import { ReferenceService } from '../service/reference.service';
import { ReferenceFormService, ReferenceFormGroup } from './reference-form.service';

@Component({
  standalone: true,
  selector: 'jhi-reference-update',
  templateUrl: './reference-update.component.html',
  styleUrl: './reference-update.component.scss',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReferenceUpdateComponent implements OnInit {
  isSaving = false;
  reference: IReference | null = null;

  userConfigsSharedCollection: IUserConfig[] = [];

  protected referenceService = inject(ReferenceService);
  protected referenceFormService = inject(ReferenceFormService);
  protected userConfigService = inject(UserConfigService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReferenceFormGroup = this.referenceFormService.createReferenceFormGroup();

  compareUserConfig = (o1: IUserConfig | null, o2: IUserConfig | null): boolean => this.userConfigService.compareUserConfig(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reference }) => {
      this.reference = reference;
      if (reference) {
        this.updateForm(reference);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reference = this.referenceFormService.getReference(this.editForm);
    if (reference.id !== null) {
      this.subscribeToSaveResponse(this.referenceService.update(reference));
    } else {
      this.subscribeToSaveResponse(this.referenceService.create(reference));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReference>>): void {
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

  protected updateForm(reference: IReference): void {
    this.reference = reference;
    this.referenceFormService.resetForm(this.editForm, reference);

    this.userConfigsSharedCollection = this.userConfigService.addUserConfigToCollectionIfMissing<IUserConfig>(
      this.userConfigsSharedCollection,
      reference.userConfig,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userConfigService
      .query()
      .pipe(map((res: HttpResponse<IUserConfig[]>) => res.body ?? []))
      .pipe(
        map((userConfigs: IUserConfig[]) =>
          this.userConfigService.addUserConfigToCollectionIfMissing<IUserConfig>(userConfigs, this.reference?.userConfig),
        ),
      )
      .subscribe((userConfigs: IUserConfig[]) => (this.userConfigsSharedCollection = userConfigs));
  }
}
