import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IContext } from '../context.model';
import { ContextService } from '../service/context.service';

@Component({
  standalone: true,
  templateUrl: './context-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
  styleUrls: ['../../../shared/table-scss-shared.scss'],
})
export class ContextDeleteDialogComponent {
  context?: IContext;

  protected contextService = inject(ContextService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contextService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
