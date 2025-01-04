import { IUserConfig } from 'app/entities/user-config/user-config.model';

export interface ITechnology {
  id: number;
  name?: string | null;
  version?: string | null;
  userConfigs?: Pick<IUserConfig, 'id' | 'username'>[] | null;
}

export type NewTechnology = Omit<ITechnology, 'id'> & { id: null };
