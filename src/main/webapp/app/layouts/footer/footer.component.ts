import { Component } from '@angular/core';
import { TranslateDirective } from 'app/shared/language';
import { SideNavbarComponent } from '../side-navbar/side-navbar.component';

@Component({
  standalone: true,
  selector: 'jhi-footer',
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.scss',
  imports: [TranslateDirective, SideNavbarComponent],
})
export default class FooterComponent {}
