import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IUserConfig } from '../user-config.model';

@Component({
  standalone: true,
  selector: 'jhi-user-config-detail',
  templateUrl: './user-config-detail.component.html',
  styleUrls: ['./user-config-detail.component.scss', '../../../shared/table-scss-shared.scss'],
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class UserConfigDetailComponent {
  userConfig = input<IUserConfig | null>(null);

  previousState(): void {
    window.history.back();
  }
}
