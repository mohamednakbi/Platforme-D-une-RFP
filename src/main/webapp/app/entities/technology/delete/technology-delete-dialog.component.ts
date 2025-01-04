import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITechnology } from '../technology.model';
import { TechnologyService } from '../service/technology.service';

@Component({
  standalone: true,
  templateUrl: './technology-delete-dialog.component.html',
  styleUrls: ['../../../shared/table-scss-shared.scss'],
  imports: [SharedModule, FormsModule],
})
export class TechnologyDeleteDialogComponent {
  technology?: ITechnology;

  protected technologyService = inject(TechnologyService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.technologyService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
