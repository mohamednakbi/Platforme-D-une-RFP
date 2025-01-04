import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUserConfig } from 'app/entities/user-config/user-config.model';
import { UserConfigService } from 'app/entities/user-config/service/user-config.service';
import { IContext } from '../context.model';
import { ContextService } from '../service/context.service';
import { ContextFormService, ContextFormGroup } from './context-form.service';

@Component({
  standalone: true,
  selector: 'jhi-context-update',
  templateUrl: './context-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
  styleUrls: ['../../../shared/add-update-entity.scss'],
})
export class ContextUpdateComponent implements OnInit {
  isSaving = false;
  context: IContext | null = null;

  userConfigsSharedCollection: IUserConfig[] = [];

  protected contextService = inject(ContextService);
  protected contextFormService = inject(ContextFormService);
  protected userConfigService = inject(UserConfigService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ContextFormGroup = this.contextFormService.createContextFormGroup();

  compareUserConfig = (o1: IUserConfig | null, o2: IUserConfig | null): boolean => this.userConfigService.compareUserConfig(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ context }) => {
      this.context = context;
      if (context) {
        this.updateForm(context);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const context = this.contextFormService.getContext(this.editForm);
    if (context.id !== null) {
      this.subscribeToSaveResponse(this.contextService.update(context));
    } else {
      this.subscribeToSaveResponse(this.contextService.create(context));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContext>>): void {
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

  protected updateForm(context: IContext): void {
    this.context = context;
    this.contextFormService.resetForm(this.editForm, context);

    this.userConfigsSharedCollection = this.userConfigService.addUserConfigToCollectionIfMissing<IUserConfig>(
      this.userConfigsSharedCollection,
      context.userConfig,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userConfigService
      .query()
      .pipe(map((res: HttpResponse<IUserConfig[]>) => res.body ?? []))
      .pipe(
        map((userConfigs: IUserConfig[]) =>
          this.userConfigService.addUserConfigToCollectionIfMissing<IUserConfig>(userConfigs, this.context?.userConfig),
        ),
      )
      .subscribe((userConfigs: IUserConfig[]) => (this.userConfigsSharedCollection = userConfigs));
  }
}
