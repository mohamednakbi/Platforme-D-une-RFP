import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUserConfig } from '../user-config.model';
import { UserConfigService } from '../service/user-config.service';

@Component({
  standalone: true,
  templateUrl: './user-config-delete-dialog.component.html',
  styleUrls: ['./user-config-delete-dialog.component.scss', '../../../shared/table-scss-shared.scss'],
  imports: [SharedModule, FormsModule],
})
export class UserConfigDeleteDialogComponent {
  userConfig?: IUserConfig;

  protected userConfigService = inject(UserConfigService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userConfigService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
