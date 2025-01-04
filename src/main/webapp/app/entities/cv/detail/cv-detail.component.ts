import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ICV } from '../cv.model';

@Component({
  standalone: true,
  selector: 'jhi-cv-detail',
  templateUrl: './cv-detail.component.html',
  styleUrls: ['./cv-detail.component.scss', '../../../shared/table-scss-shared.scss'],

  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CVDetailComponent {
  cV = input<ICV | null>(null);

  previousState(): void {
    window.history.back();
  }
}
