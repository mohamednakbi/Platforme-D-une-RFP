import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRole } from '../role.model';
import { RoleService } from '../service/role.service';

const roleResolve = (route: ActivatedRouteSnapshot): Observable<null | IRole> => {
  const id = route.params['id'];
  if (id) {
    return inject(RoleService)
      .find(id)
      .pipe(
        mergeMap((role: HttpResponse<IRole>) => {
          if (role.body) {
            return of(role.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default roleResolve;
