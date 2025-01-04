import { Component, inject, OnInit, signal } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterLinkActive } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { MatListItem, MatNavList } from '@angular/material/list';
import { MatDivider } from '@angular/material/divider';
import { MatIcon } from '@angular/material/icon';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatButtonModule } from '@angular/material/button';
import { NgClass, NgIf } from '@angular/common';
import { EntityNavbarItems } from '../../entities/entity-navbar-items';
import { LoginService } from '../../login/login.service';
import NavbarItem from '../navbar/navbar-item.model';
import { TranslateService } from '@ngx-translate/core';
import { StateStorageService } from '../../core/auth/state-storage.service';
import { ProfileService } from '../profiles/profile.service';
import { LANGUAGES } from '../../config/language.constants';
import { AccountService } from '../../core/auth/account.service';
import { filter } from 'rxjs/operators';
import HasAnyAuthorityDirective from '../../shared/auth/has-any-authority.directive';
@Component({
  selector: 'jhi-side-navbar',
  standalone: true,
  imports: [
    FaIconComponent,
    RouterLink,
    RouterLinkActive,
    MatNavList,
    MatDivider,
    MatIcon,
    MatListItem,
    MatSidenavModule,
    MatButtonModule,
    NgClass,
    NgIf,
    HasAnyAuthorityDirective,
  ],
  templateUrl: './side-navbar.component.html',
  styleUrl: './side-navbar.component.scss',
})
export class SideNavbarComponent implements OnInit {
  inProduction?: boolean;
  openAPIEnabled?: boolean;
  version = '';
  account = inject(AccountService).trackCurrentAccount();
  private loginService = inject(LoginService);
  private router = inject(Router);
  private stateStorageService = inject(StateStorageService);
  private profileService = inject(ProfileService);
  entitiesSidevbarItems: NavbarItem[] = [];

  isOpen: boolean = true;
  currentRoute: string = '';
  constructor() {
    this.router.events.pipe(filter(this.isNavigationEnd)).subscribe((event: NavigationEnd) => {
      this.currentRoute = event.urlAfterRedirects.split('?')[0];
    });
    this.toggleSidenav();
  }
  isNavigationEnd(event: any): event is NavigationEnd {
    return event instanceof NavigationEnd;
  }

  toggleSidenav() {
    this.isOpen = !this.isOpen;

    const container = document.querySelector('.container');
    if (container) {
      if (this.isOpen) {
        container.classList.add('move-right');
      } else {
        container.classList.remove('move-right');
      }
    } else {
      console.error('Container not found!');
    }
  }

  ngOnInit(): void {
    this.entitiesSidevbarItems = EntityNavbarItems;
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });
  }

  login(): void {
    this.loginService.login();
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['']);
  }
}
