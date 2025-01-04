import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRole, NewRole } from '../role.model';

export type PartialUpdateRole = Partial<IRole> & Pick<IRole, 'id'>;

export type EntityResponseType = HttpResponse<IRole>;
export type EntityArrayResponseType = HttpResponse<IRole[]>;

@Injectable({ providedIn: 'root' })
export class RoleService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/roles');

  create(role: NewRole): Observable<EntityResponseType> {
    return this.http.post<IRole>(this.resourceUrl, role, { observe: 'response' });
  }

  update(role: IRole): Observable<EntityResponseType> {
    return this.http.put<IRole>(`${this.resourceUrl}/${this.getRoleIdentifier(role)}`, role, { observe: 'response' });
  }

  partialUpdate(role: PartialUpdateRole): Observable<EntityResponseType> {
    return this.http.patch<IRole>(`${this.resourceUrl}/${this.getRoleIdentifier(role)}`, role, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRole>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRole[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRoleIdentifier(role: Pick<IRole, 'id'>): number {
    return role.id;
  }

  compareRole(o1: Pick<IRole, 'id'> | null, o2: Pick<IRole, 'id'> | null): boolean {
    return o1 && o2 ? this.getRoleIdentifier(o1) === this.getRoleIdentifier(o2) : o1 === o2;
  }

  addRoleToCollectionIfMissing<Type extends Pick<IRole, 'id'>>(
    roleCollection: Type[],
    ...rolesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const roles: Type[] = rolesToCheck.filter(isPresent);
    if (roles.length > 0) {
      const roleCollectionIdentifiers = roleCollection.map(roleItem => this.getRoleIdentifier(roleItem));
      const rolesToAdd = roles.filter(roleItem => {
        const roleIdentifier = this.getRoleIdentifier(roleItem);
        if (roleCollectionIdentifiers.includes(roleIdentifier)) {
          return false;
        }
        roleCollectionIdentifiers.push(roleIdentifier);
        return true;
      });
      return [...rolesToAdd, ...roleCollection];
    }
    return roleCollection;
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRole[]>(`${this.resourceUrl}/search/name`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => res));
  }
}
