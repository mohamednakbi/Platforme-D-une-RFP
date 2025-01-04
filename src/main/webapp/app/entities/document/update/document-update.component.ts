import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUserConfig } from 'app/entities/user-config/user-config.model';
import { UserConfigService } from 'app/entities/user-config/service/user-config.service';
import { DocumentType } from 'app/entities/enumerations/document-type.model';
import { DocumentService } from '../service/document.service';
import { IDocument } from '../document.model';
import { DocumentFormService, DocumentFormGroup } from './document-form.service';

@Component({
  standalone: true,
  selector: 'jhi-document-update',
  templateUrl: './document-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DocumentUpdateComponent implements OnInit {
  isSaving = false;
  document: IDocument | null = null;
  documentTypeValues = Object.keys(DocumentType);

  userConfigsSharedCollection: IUserConfig[] = [];

  protected documentService = inject(DocumentService);
  protected documentFormService = inject(DocumentFormService);
  protected userConfigService = inject(UserConfigService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentFormGroup = this.documentFormService.createDocumentFormGroup();

  compareUserConfig = (o1: IUserConfig | null, o2: IUserConfig | null): boolean => this.userConfigService.compareUserConfig(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ document }) => {
      this.document = document;
      if (document) {
        this.updateForm(document);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const document = this.documentFormService.getDocument(this.editForm);
    if (document.id !== null) {
      this.subscribeToSaveResponse(this.documentService.update(document));
    } else {
      this.subscribeToSaveResponse(this.documentService.create(document));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocument>>): void {
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

  protected updateForm(document: IDocument): void {
    this.document = document;
    this.documentFormService.resetForm(this.editForm, document);

    this.userConfigsSharedCollection = this.userConfigService.addUserConfigToCollectionIfMissing<IUserConfig>(
      this.userConfigsSharedCollection,
      document.userConfig,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userConfigService
      .query()
      .pipe(map((res: HttpResponse<IUserConfig[]>) => res.body ?? []))
      .pipe(
        map((userConfigs: IUserConfig[]) =>
          this.userConfigService.addUserConfigToCollectionIfMissing<IUserConfig>(userConfigs, this.document?.userConfig),
        ),
      )
      .subscribe((userConfigs: IUserConfig[]) => (this.userConfigsSharedCollection = userConfigs));
  }
}
