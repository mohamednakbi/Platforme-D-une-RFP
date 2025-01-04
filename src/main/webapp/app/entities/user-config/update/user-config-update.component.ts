import { Component, ElementRef, inject, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRole } from 'app/entities/role/role.model';
import { RoleService } from 'app/entities/role/service/role.service';
import { ITechnology } from 'app/entities/technology/technology.model';
import { TechnologyService } from 'app/entities/technology/service/technology.service';
import { UserConfigService } from '../service/user-config.service';
import { IUserConfig } from '../user-config.model';
import { UserConfigFormService, UserConfigFormGroup } from './user-config-form.service';

@Component({
  standalone: true,
  selector: 'jhi-user-config-update',
  templateUrl: './user-config-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
  styleUrls: ['../../../shared/add-update-entity.scss'],
})
export class UserConfigUpdateComponent implements OnInit {
  constructor(
    private el: ElementRef,
    private formBuilder: FormBuilder,
  ) {}

  isSaving = false;
  userConfig: IUserConfig | null = null;

  rolesSharedCollection: IRole[] = [];
  technologiesSharedCollection: ITechnology[] = [];

  protected userConfigService = inject(UserConfigService);
  protected userConfigFormService = inject(UserConfigFormService);
  protected roleService = inject(RoleService);
  protected technologyService = inject(TechnologyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UserConfigFormGroup = this.userConfigFormService.createUserConfigFormGroup();

  compareRole = (o1: IRole | null, o2: IRole | null): boolean => this.roleService.compareRole(o1, o2);

  compareTechnology = (o1: ITechnology | null, o2: ITechnology | null): boolean => this.technologyService.compareTechnology(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userConfig }) => {
      this.userConfig = userConfig;
      if (userConfig) {
        this.updateForm(userConfig);
      }

      this.loadRelationshipsOptions();
    });
    this.el.nativeElement.setAttribute('type', 'password');
  }

  previousState(): void {
    //  window.history.back();
    window.location.reload();
    alert('Données ajoutées avec succès!');
  }
  previousState1(): void {
      window.history.back();

  }

  save(): void {
    this.isSaving = true;
    const userConfig = this.userConfigFormService.getUserConfig(this.editForm);

    if (!this.checkPasswordMatch()) {
      alert('Les mots de passe ne correspondent pas.');
    } else {
      if (userConfig.id !== null) {
        this.subscribeToSaveResponse(this.userConfigService.update(userConfig));
      } else {
        this.subscribeToSaveResponse(this.userConfigService.create(userConfig));
      }
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserConfig>>): void {
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

  protected updateForm(userConfig: IUserConfig): void {
    this.userConfig = userConfig;
    this.userConfigFormService.resetForm(this.editForm, userConfig);

    this.rolesSharedCollection = this.roleService.addRoleToCollectionIfMissing<IRole>(this.rolesSharedCollection, userConfig.role);
    this.technologiesSharedCollection = this.technologyService.addTechnologyToCollectionIfMissing<ITechnology>(
      this.technologiesSharedCollection,
      ...(userConfig.technologys ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.roleService
      .query()
      .pipe(map((res: HttpResponse<IRole[]>) => res.body ?? []))
      .pipe(map((roles: IRole[]) => this.roleService.addRoleToCollectionIfMissing<IRole>(roles, this.userConfig?.role)))
      .subscribe((roles: IRole[]) => (this.rolesSharedCollection = roles));

    this.technologyService
      .query()
      .pipe(map((res: HttpResponse<ITechnology[]>) => res.body ?? []))
      .pipe(
        map((technologies: ITechnology[]) =>
          this.technologyService.addTechnologyToCollectionIfMissing<ITechnology>(technologies, ...(this.userConfig?.technologys ?? [])),
        ),
      )
      .subscribe((technologies: ITechnology[]) => (this.technologiesSharedCollection = technologies));
  }

  checkPasswordMatch() {
    const password = (document.getElementById('field_password') as HTMLInputElement).value;
    const confirmPassword = (document.getElementById('field_confirmPassword') as HTMLInputElement).value;

    console.log(password, 'confirmation', confirmPassword);

    return password === confirmPassword;
  }
}
