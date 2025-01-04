import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICV, NewCV } from '../cv.model';
import { IContext } from '../../context/context.model';
import { map } from 'rxjs/operators';

export type PartialUpdateCV = Partial<ICV> & Pick<ICV, 'id'>;

export type EntityResponseType = HttpResponse<ICV>;
export type EntityArrayResponseType = HttpResponse<ICV[]>;

@Injectable({ providedIn: 'root' })
export class CVService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cvs');

  create(cV: NewCV): Observable<EntityResponseType> {
    return this.http.post<ICV>(this.resourceUrl, cV, { observe: 'response' });
  }

  update(cV: ICV): Observable<EntityResponseType> {
    return this.http.put<ICV>(`${this.resourceUrl}/${this.getCVIdentifier(cV)}`, cV, { observe: 'response' });
  }

  partialUpdate(cV: PartialUpdateCV): Observable<EntityResponseType> {
    return this.http.patch<ICV>(`${this.resourceUrl}/${this.getCVIdentifier(cV)}`, cV, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICV>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICV[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCVIdentifier(cV: Pick<ICV, 'id'>): number {
    return cV.id;
  }

  compareCV(o1: Pick<ICV, 'id'> | null, o2: Pick<ICV, 'id'> | null): boolean {
    return o1 && o2 ? this.getCVIdentifier(o1) === this.getCVIdentifier(o2) : o1 === o2;
  }

  addCVToCollectionIfMissing<Type extends Pick<ICV, 'id'>>(cVCollection: Type[], ...cVSToCheck: (Type | null | undefined)[]): Type[] {
    const cVS: Type[] = cVSToCheck.filter(isPresent);
    if (cVS.length > 0) {
      const cVCollectionIdentifiers = cVCollection.map(cVItem => this.getCVIdentifier(cVItem));
      const cVSToAdd = cVS.filter(cVItem => {
        const cVIdentifier = this.getCVIdentifier(cVItem);
        if (cVCollectionIdentifiers.includes(cVIdentifier)) {
          return false;
        }
        cVCollectionIdentifiers.push(cVIdentifier);
        return true;
      });
      return [...cVSToAdd, ...cVCollection];
    }
    return cVCollection;
  }
  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICV[]>(`${this.resourceUrl}/search/titlee`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => res));
  }
}
