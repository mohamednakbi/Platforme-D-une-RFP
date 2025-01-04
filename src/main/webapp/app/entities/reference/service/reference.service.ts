import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReference, NewReference } from '../reference.model';
import { ICV } from '../../cv/cv.model';
import { map } from 'rxjs/operators';

export type PartialUpdateReference = Partial<IReference> & Pick<IReference, 'id'>;

export type EntityResponseType = HttpResponse<IReference>;
export type EntityArrayResponseType = HttpResponse<IReference[]>;

@Injectable({ providedIn: 'root' })
export class ReferenceService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/references');

  create(reference: NewReference): Observable<EntityResponseType> {
    return this.http.post<IReference>(this.resourceUrl, reference, { observe: 'response' });
  }

  update(reference: IReference): Observable<EntityResponseType> {
    return this.http.put<IReference>(`${this.resourceUrl}/${this.getReferenceIdentifier(reference)}`, reference, { observe: 'response' });
  }

  partialUpdate(reference: PartialUpdateReference): Observable<EntityResponseType> {
    return this.http.patch<IReference>(`${this.resourceUrl}/${this.getReferenceIdentifier(reference)}`, reference, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReference>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReference[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getReferenceIdentifier(reference: Pick<IReference, 'id'>): number {
    return reference.id;
  }

  compareReference(o1: Pick<IReference, 'id'> | null, o2: Pick<IReference, 'id'> | null): boolean {
    return o1 && o2 ? this.getReferenceIdentifier(o1) === this.getReferenceIdentifier(o2) : o1 === o2;
  }

  addReferenceToCollectionIfMissing<Type extends Pick<IReference, 'id'>>(
    referenceCollection: Type[],
    ...referencesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const references: Type[] = referencesToCheck.filter(isPresent);
    if (references.length > 0) {
      const referenceCollectionIdentifiers = referenceCollection.map(referenceItem => this.getReferenceIdentifier(referenceItem));
      const referencesToAdd = references.filter(referenceItem => {
        const referenceIdentifier = this.getReferenceIdentifier(referenceItem);
        if (referenceCollectionIdentifiers.includes(referenceIdentifier)) {
          return false;
        }
        referenceCollectionIdentifiers.push(referenceIdentifier);
        return true;
      });
      return [...referencesToAdd, ...referenceCollection];
    }
    return referenceCollection;
  }
  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IReference[]>(`${this.resourceUrl}/search/title`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => res));
  }
}
