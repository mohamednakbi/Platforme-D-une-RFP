import { Component, NgZone, inject, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { combineLatest, filter, Observable, Subscription, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { sortStateSignal, SortDirective, SortByDirective, type SortState, SortService } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { IRole } from '../role.model';
import { EntityArrayResponseType, RoleService } from '../service/role.service';
import { RoleDeleteDialogComponent } from '../delete/role-delete-dialog.component';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MaterialModule } from '../../../shared/material-module';
import { RoleUpdateComponent } from '../update/role-update.component';

@Component({
  standalone: true,
  selector: 'jhi-role',
  templateUrl: './role.component.html',
  styleUrls: ['../../../shared/table-scss-shared.scss', './role.component.scss'],
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    ItemCountComponent,
    MatDialogModule,
    MaterialModule,
  ],
})
export class RoleComponent implements OnInit {
  subscription: Subscription | null = null;
  roles?: IRole[];
  filteredRoles?: IRole[];
  isLoading = false;
  roleName: string = '';
  page = 1;
  itemsPerPage = 10;
  totalItems = 0;

  sortState = sortStateSignal({});

  public router = inject(Router);
  protected roleService = inject(RoleService);
  protected activatedRoute = inject(ActivatedRoute);
  protected sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);

  constructor(private dialog: MatDialog) {}

  trackId = (_index: number, item: IRole): number => this.roleService.getRoleIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
  }

  delete(role: IRole): void {
    const modalRef = this.modalService.open(RoleDeleteDialogComponent, { size: 'lg', backdrop: 'static', centered: true });
    modalRef.componentInstance.role = role;
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }
  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => this.onResponseSuccess(res),
    });
  }
  addRole(): void {
    const modalRef = this.modalService.open(RoleUpdateComponent, { size: 'lg', backdrop: 'static', centered: true });
    modalRef.componentInstance.role = {}; // Initialize with a new empty object or with default values if necessary
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page, event);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState());
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.roles = dataFromBody;
    this.filteredRoles = this.roleName.trim() !== '' ? dataFromBody : this.roles;
  }

  protected fillComponentAttributesFromResponseBody(data: IRole[] | null): IRole[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    const { page } = this;
    this.isLoading = true;
    const pageToLoad: number = page;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    return this.roleService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page: number, sortState: SortState): void {
    const queryParamsObj = {
      page,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(sortState),
    };

    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }

  searchRolesByName(): void {
    if (this.roleName.trim() !== '') {
      this.isLoading = true;
      console.log('Searching for roles with name:', this.roleName);
      const queryObject = {
        name: this.roleName,
        page: 0,
        size: this.itemsPerPage,
      };
      this.roleService.search(queryObject).subscribe({
        next: (res: EntityArrayResponseType) => {
          console.log('Search response:', res.body);
          this.onResponseSuccess(res);
          this.isLoading = false;
        },
        error: err => {
          console.error('Search error:', err);
          this.isLoading = false;
        },
      });
    } else {
      console.log('Search input is empty, resetting filteredRoles to roles');
      this.filteredRoles = this.roles;
    }
  }
}
