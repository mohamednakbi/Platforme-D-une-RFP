import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IRole } from '../role.model';

@Component({
  standalone: true,
  selector: 'jhi-role-detail',
  templateUrl: './role-detail.component.html',
  styleUrls: ['../../../shared/table-scss-shared.scss', './role-detail.component.scss'],
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RoleDetailComponent {
  role = input<IRole | null>(null);

  previousState(): void {
    window.history.back();
  }
}
