import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITechnology } from '../technology.model';
import { TechnologyService } from '../service/technology.service';

const technologyResolve = (route: ActivatedRouteSnapshot): Observable<null | ITechnology> => {
  const id = route.params['id'];
  if (id) {
    return inject(TechnologyService)
      .find(id)
      .pipe(
        mergeMap((technology: HttpResponse<ITechnology>) => {
          if (technology.body) {
            return of(technology.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default technologyResolve;
