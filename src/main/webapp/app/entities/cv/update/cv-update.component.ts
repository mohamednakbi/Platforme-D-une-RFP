import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUserConfig } from 'app/entities/user-config/user-config.model';
import { UserConfigService } from 'app/entities/user-config/service/user-config.service';
import { ICV } from '../cv.model';
import { CVService } from '../service/cv.service';
import { CVFormService, CVFormGroup } from './cv-form.service';

@Component({
  standalone: true,
  selector: 'jhi-cv-update',
  templateUrl: './cv-update.component.html',
  styleUrl: './cv-update.component.scss',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CVUpdateComponent implements OnInit {
  isSaving = false;
  cV: ICV | null = null;

  userConfigsCollection: IUserConfig[] = [];

  protected cVService = inject(CVService);
  protected cVFormService = inject(CVFormService);
  protected userConfigService = inject(UserConfigService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CVFormGroup = this.cVFormService.createCVFormGroup();

  compareUserConfig = (o1: IUserConfig | null, o2: IUserConfig | null): boolean => this.userConfigService.compareUserConfig(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cV }) => {
      this.cV = cV;
      if (cV) {
        this.updateForm(cV);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cV = this.cVFormService.getCV(this.editForm);
    if (cV.id !== null) {
      this.subscribeToSaveResponse(this.cVService.update(cV));
    } else {
      this.subscribeToSaveResponse(this.cVService.create(cV));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICV>>): void {
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

  protected updateForm(cV: ICV): void {
    this.cV = cV;
    this.cVFormService.resetForm(this.editForm, cV);

    this.userConfigsCollection = this.userConfigService.addUserConfigToCollectionIfMissing<IUserConfig>(
      this.userConfigsCollection,
      cV.userConfig,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userConfigService
      .query({ filter: 'cv-is-null' })
      .pipe(map((res: HttpResponse<IUserConfig[]>) => res.body ?? []))
      .pipe(
        map((userConfigs: IUserConfig[]) =>
          this.userConfigService.addUserConfigToCollectionIfMissing<IUserConfig>(userConfigs, this.cV?.userConfig),
        ),
      )
      .subscribe((userConfigs: IUserConfig[]) => (this.userConfigsCollection = userConfigs));
  }
}
