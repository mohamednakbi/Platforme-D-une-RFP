import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRole } from '../role.model';
import { RoleService } from '../service/role.service';

@Component({
  standalone: true,
  templateUrl: './role-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
  styleUrls: ['../../../shared/table-scss-shared.scss'],
})
export class RoleDeleteDialogComponent {
  role?: IRole;

  protected roleService = inject(RoleService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.roleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
