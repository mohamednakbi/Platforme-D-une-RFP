import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IReference } from '../reference.model';
import { ReferenceService } from '../service/reference.service';

@Component({
  standalone: true,
  templateUrl: './reference-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
  styleUrls: ['../../../shared/table-scss-shared.scss'],
})
export class ReferenceDeleteDialogComponent {
  reference?: IReference;

  protected referenceService = inject(ReferenceService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.referenceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
