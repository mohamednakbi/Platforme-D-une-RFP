import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserConfig, NewUserConfig } from '../user-config.model';
import { ICV } from '../../cv/cv.model';
import { map } from 'rxjs/operators';

export type PartialUpdateUserConfig = Partial<IUserConfig> & Pick<IUserConfig, 'id'>;

export type EntityResponseType = HttpResponse<IUserConfig>;
export type EntityArrayResponseType = HttpResponse<IUserConfig[]>;

@Injectable({ providedIn: 'root' })
export class UserConfigService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-configs');

  create(userConfig: NewUserConfig): Observable<EntityResponseType> {
    return this.http.post<IUserConfig>(this.resourceUrl, userConfig, { observe: 'response' });
  }

  update(userConfig: IUserConfig): Observable<EntityResponseType> {
    return this.http.put<IUserConfig>(`${this.resourceUrl}/${this.getUserConfigIdentifier(userConfig)}`, userConfig, {
      observe: 'response',
    });
  }

  partialUpdate(userConfig: PartialUpdateUserConfig): Observable<EntityResponseType> {
    return this.http.patch<IUserConfig>(`${this.resourceUrl}/${this.getUserConfigIdentifier(userConfig)}`, userConfig, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserConfig>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserConfig[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserConfigIdentifier(userConfig: Pick<IUserConfig, 'id'>): number {
    return userConfig.id;
  }

  compareUserConfig(o1: Pick<IUserConfig, 'id'> | null, o2: Pick<IUserConfig, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserConfigIdentifier(o1) === this.getUserConfigIdentifier(o2) : o1 === o2;
  }

  addUserConfigToCollectionIfMissing<Type extends Pick<IUserConfig, 'id'>>(
    userConfigCollection: Type[],
    ...userConfigsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userConfigs: Type[] = userConfigsToCheck.filter(isPresent);
    if (userConfigs.length > 0) {
      const userConfigCollectionIdentifiers = userConfigCollection.map(userConfigItem => this.getUserConfigIdentifier(userConfigItem));
      const userConfigsToAdd = userConfigs.filter(userConfigItem => {
        const userConfigIdentifier = this.getUserConfigIdentifier(userConfigItem);
        if (userConfigCollectionIdentifiers.includes(userConfigIdentifier)) {
          return false;
        }
        userConfigCollectionIdentifiers.push(userConfigIdentifier);
        return true;
      });
      return [...userConfigsToAdd, ...userConfigCollection];
    }
    return userConfigCollection;
  }
  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUserConfig[]>(`${this.resourceUrl}/search/username`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => res));
  }
}
