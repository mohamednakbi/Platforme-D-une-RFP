import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITechnology, NewTechnology } from '../technology.model';
import { IRole } from '../../role/role.model';
import { map } from 'rxjs/operators';

export type PartialUpdateTechnology = Partial<ITechnology> & Pick<ITechnology, 'id'>;

export type EntityResponseType = HttpResponse<ITechnology>;
export type EntityArrayResponseType = HttpResponse<ITechnology[]>;

@Injectable({ providedIn: 'root' })
export class TechnologyService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/technologies');

  create(technology: NewTechnology): Observable<EntityResponseType> {
    return this.http.post<ITechnology>(this.resourceUrl, technology, { observe: 'response' });
  }

  update(technology: ITechnology): Observable<EntityResponseType> {
    return this.http.put<ITechnology>(`${this.resourceUrl}/${this.getTechnologyIdentifier(technology)}`, technology, {
      observe: 'response',
    });
  }

  partialUpdate(technology: PartialUpdateTechnology): Observable<EntityResponseType> {
    return this.http.patch<ITechnology>(`${this.resourceUrl}/${this.getTechnologyIdentifier(technology)}`, technology, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITechnology>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITechnology[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTechnologyIdentifier(technology: Pick<ITechnology, 'id'>): number {
    return technology.id;
  }

  compareTechnology(o1: Pick<ITechnology, 'id'> | null, o2: Pick<ITechnology, 'id'> | null): boolean {
    return o1 && o2 ? this.getTechnologyIdentifier(o1) === this.getTechnologyIdentifier(o2) : o1 === o2;
  }

  addTechnologyToCollectionIfMissing<Type extends Pick<ITechnology, 'id'>>(
    technologyCollection: Type[],
    ...technologiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const technologies: Type[] = technologiesToCheck.filter(isPresent);
    if (technologies.length > 0) {
      const technologyCollectionIdentifiers = technologyCollection.map(technologyItem => this.getTechnologyIdentifier(technologyItem));
      const technologiesToAdd = technologies.filter(technologyItem => {
        const technologyIdentifier = this.getTechnologyIdentifier(technologyItem);
        if (technologyCollectionIdentifiers.includes(technologyIdentifier)) {
          return false;
        }
        technologyCollectionIdentifiers.push(technologyIdentifier);
        return true;
      });
      return [...technologiesToAdd, ...technologyCollection];
    }
    return technologyCollection;
  }
  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITechnology[]>(`${this.resourceUrl}/search/namee`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => res));
  }
}
