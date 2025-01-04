import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUserConfig } from 'app/entities/user-config/user-config.model';
import { UserConfigService } from 'app/entities/user-config/service/user-config.service';
import { ITechnology } from '../technology.model';
import { TechnologyService } from '../service/technology.service';
import { TechnologyFormService, TechnologyFormGroup } from './technology-form.service';

@Component({
  standalone: true,
  selector: 'jhi-technology-update',
  templateUrl: './technology-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
  styleUrls: ['../../../shared/add-update-entity.scss'],
})
export class TechnologyUpdateComponent implements OnInit {
  isSaving = false;
  technology: ITechnology | null = null;

  userConfigsSharedCollection: IUserConfig[] = [];

  protected technologyService = inject(TechnologyService);
  protected technologyFormService = inject(TechnologyFormService);
  protected userConfigService = inject(UserConfigService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TechnologyFormGroup = this.technologyFormService.createTechnologyFormGroup();

  compareUserConfig = (o1: IUserConfig | null, o2: IUserConfig | null): boolean => this.userConfigService.compareUserConfig(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ technology }) => {
      this.technology = technology;
      if (technology) {
        this.updateForm(technology);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const technology = this.technologyFormService.getTechnology(this.editForm);
    if (technology.id !== null) {
      this.subscribeToSaveResponse(this.technologyService.update(technology));
    } else {
      this.subscribeToSaveResponse(this.technologyService.create(technology));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITechnology>>): void {
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

  protected updateForm(technology: ITechnology): void {
    this.technology = technology;
    this.technologyFormService.resetForm(this.editForm, technology);

    this.userConfigsSharedCollection = this.userConfigService.addUserConfigToCollectionIfMissing<IUserConfig>(
      this.userConfigsSharedCollection,
      ...(technology.userConfigs ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userConfigService
      .query()
      .pipe(map((res: HttpResponse<IUserConfig[]>) => res.body ?? []))
      .pipe(
        map((userConfigs: IUserConfig[]) =>
          this.userConfigService.addUserConfigToCollectionIfMissing<IUserConfig>(userConfigs, ...(this.technology?.userConfigs ?? [])),
        ),
      )
      .subscribe((userConfigs: IUserConfig[]) => (this.userConfigsSharedCollection = userConfigs));
  }
}
