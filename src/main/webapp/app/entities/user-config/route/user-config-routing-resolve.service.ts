import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserConfig } from '../user-config.model';
import { UserConfigService } from '../service/user-config.service';

const userConfigResolve = (route: ActivatedRouteSnapshot): Observable<null | IUserConfig> => {
  const id = route.params['id'];
  if (id) {
    return inject(UserConfigService)
      .find(id)
      .pipe(
        mergeMap((userConfig: HttpResponse<IUserConfig>) => {
          if (userConfig.body) {
            return of(userConfig.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default userConfigResolve;
