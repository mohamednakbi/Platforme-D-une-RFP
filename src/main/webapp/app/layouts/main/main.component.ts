import { Component, inject, OnInit, RendererFactory2, Renderer2, signal } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import dayjs from 'dayjs/esm';

import { AccountService } from 'app/core/auth/account.service';
import { AppPageTitleStrategy } from 'app/app-page-title-strategy';
import FooterComponent from '../footer/footer.component';
import PageRibbonComponent from '../profiles/page-ribbon.component';
import { SideNavbarComponent } from '../side-navbar/side-navbar.component';
import { Account } from '../../core/auth/account.model';
import { NgIf } from '@angular/common';

@Component({
  standalone: true,
  selector: 'jhi-main',
  templateUrl: './main.component.html',
  providers: [AppPageTitleStrategy],
  imports: [
    RouterOutlet,
    FooterComponent,
    PageRibbonComponent,
    SideNavbarComponent,
    FooterComponent,
    FooterComponent,
    NgIf,
    FooterComponent,
  ],
})
export default class MainComponent implements OnInit {
  private renderer: Renderer2;

  isOpen: boolean = true;

  toggleSidenav() {
    this.isOpen = !this.isOpen;
  }

  private router = inject(Router);
  private appPageTitleStrategy = inject(AppPageTitleStrategy);
  private accountService = inject(AccountService);
  private translateService = inject(TranslateService);
  private rootRenderer = inject(RendererFactory2);
  isAutheauntificate: boolean = false;
  account = signal<Account | null>(null);
  constructor() {
    this.renderer = this.rootRenderer.createRenderer(document.querySelector('html'), null);
    this.accountService.identity().subscribe(account => this.account.set(account));
    console.log('account', this.account);
  }

  ngOnInit(): void {
    // try to log in automatically
    this.accountService.identity().subscribe();

    this.translateService.onLangChange.subscribe((langChangeEvent: LangChangeEvent) => {
      this.appPageTitleStrategy.updateTitle(this.router.routerState.snapshot);
      dayjs.locale(langChangeEvent.lang);
      this.renderer.setAttribute(document.querySelector('html'), 'lang', langChangeEvent.lang);
    });
  }
}
