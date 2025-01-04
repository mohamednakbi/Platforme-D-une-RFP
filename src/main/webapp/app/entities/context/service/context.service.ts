import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContext, NewContext } from '../context.model';
import { ITechnology } from '../../technology/technology.model';
import { map } from 'rxjs/operators';

export type PartialUpdateContext = Partial<IContext> & Pick<IContext, 'id'>;

export type EntityResponseType = HttpResponse<IContext>;
export type EntityArrayResponseType = HttpResponse<IContext[]>;

@Injectable({ providedIn: 'root' })
export class ContextService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contexts');

  create(context: NewContext): Observable<EntityResponseType> {
    return this.http.post<IContext>(this.resourceUrl, context, { observe: 'response' });
  }

  update(context: IContext): Observable<EntityResponseType> {
    return this.http.put<IContext>(`${this.resourceUrl}/${this.getContextIdentifier(context)}`, context, { observe: 'response' });
  }

  partialUpdate(context: PartialUpdateContext): Observable<EntityResponseType> {
    return this.http.patch<IContext>(`${this.resourceUrl}/${this.getContextIdentifier(context)}`, context, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContext>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContext[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getContextIdentifier(context: Pick<IContext, 'id'>): number {
    return context.id;
  }

  compareContext(o1: Pick<IContext, 'id'> | null, o2: Pick<IContext, 'id'> | null): boolean {
    return o1 && o2 ? this.getContextIdentifier(o1) === this.getContextIdentifier(o2) : o1 === o2;
  }

  addContextToCollectionIfMissing<Type extends Pick<IContext, 'id'>>(
    contextCollection: Type[],
    ...contextsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contexts: Type[] = contextsToCheck.filter(isPresent);
    if (contexts.length > 0) {
      const contextCollectionIdentifiers = contextCollection.map(contextItem => this.getContextIdentifier(contextItem));
      const contextsToAdd = contexts.filter(contextItem => {
        const contextIdentifier = this.getContextIdentifier(contextItem);
        if (contextCollectionIdentifiers.includes(contextIdentifier)) {
          return false;
        }
        contextCollectionIdentifiers.push(contextIdentifier);
        return true;
      });
      return [...contextsToAdd, ...contextCollection];
    }
    return contextCollection;
  }
  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IContext[]>(`${this.resourceUrl}/search/namee`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => res));
  }
}
