import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICV } from '../cv.model';
import { CVService } from '../service/cv.service';

const cVResolve = (route: ActivatedRouteSnapshot): Observable<null | ICV> => {
  const id = route.params['id'];
  if (id) {
    return inject(CVService)
      .find(id)
      .pipe(
        mergeMap((cV: HttpResponse<ICV>) => {
          if (cV.body) {
            return of(cV.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default cVResolve;
