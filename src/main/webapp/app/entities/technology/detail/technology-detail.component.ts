import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITechnology } from '../technology.model';

@Component({
  standalone: true,
  selector: 'jhi-technology-detail',
  templateUrl: './technology-detail.component.html',
  styleUrls: ['./technology-detail.component.scss', '../../../shared/table-scss-shared.scss'],
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TechnologyDetailComponent {
  technology = input<ITechnology | null>(null);

  previousState(): void {
    window.history.back();
  }
}
