import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICV } from '../cv.model';
import { CVService } from '../service/cv.service';

@Component({
  standalone: true,
  templateUrl: './cv-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
  styleUrls: ['../../../shared/table-scss-shared.scss'],
})
export class CVDeleteDialogComponent {
  cV?: ICV;

  protected cVService = inject(CVService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cVService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
