import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRole } from '../role.model';
import { RoleService } from '../service/role.service';
import { RoleFormService, RoleFormGroup } from './role-form.service';

@Component({
  standalone: true,
  selector: 'jhi-role-update',
  templateUrl: './role-update.component.html',
  styleUrl: './role-update.component.scss',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RoleUpdateComponent implements OnInit {
  isSaving = false;
  role: IRole | null = null;

  protected roleService = inject(RoleService);
  protected roleFormService = inject(RoleFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RoleFormGroup = this.roleFormService.createRoleFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ role }) => {
      this.role = role;
      if (role) {
        this.updateForm(role);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const role = this.roleFormService.getRole(this.editForm);
    if (role.id !== null) {
      this.subscribeToSaveResponse(this.roleService.update(role));
    } else {
      this.subscribeToSaveResponse(this.roleService.create(role));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRole>>): void {
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

  protected updateForm(role: IRole): void {
    this.role = role;
    this.roleFormService.resetForm(this.editForm, role);
  }

  //onAuthoritiesChange(event: any): void {
  //   this.selectedAuthorities = [];
  //
  //   const options = event.target.options;
  //   for (let i = 0; i < options.length; i++) {
  //     if (options[i].selected) {
  //       this.selectedAuthorities.push(options[i].value);
  //     }
  //   }
  //
  // }
}
