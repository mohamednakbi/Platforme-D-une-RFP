import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReference } from '../reference.model';
import { ReferenceService } from '../service/reference.service';

const referenceResolve = (route: ActivatedRouteSnapshot): Observable<null | IReference> => {
  const id = route.params['id'];
  if (id) {
    return inject(ReferenceService)
      .find(id)
      .pipe(
        mergeMap((reference: HttpResponse<IReference>) => {
          if (reference.body) {
            return of(reference.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default referenceResolve;
