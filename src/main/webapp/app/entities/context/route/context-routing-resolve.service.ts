import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContext } from '../context.model';
import { ContextService } from '../service/context.service';

const contextResolve = (route: ActivatedRouteSnapshot): Observable<null | IContext> => {
  const id = route.params['id'];
  if (id) {
    return inject(ContextService)
      .find(id)
      .pipe(
        mergeMap((context: HttpResponse<IContext>) => {
          if (context.body) {
            return of(context.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default contextResolve;
