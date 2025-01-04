import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IReference } from '../reference.model';

@Component({
  standalone: true,
  selector: 'jhi-reference-detail',
  templateUrl: './reference-detail.component.html',
  styleUrls: ['../../../shared/table-scss-shared.scss', './reference.component.scss'],
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ReferenceDetailComponent {
  reference = input<IReference | null>(null);

  previousState(): void {
    window.history.back();
  }
}
