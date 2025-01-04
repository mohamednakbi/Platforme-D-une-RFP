import { IUserConfig } from 'app/entities/user-config/user-config.model';

export interface IContext {
  id: number;
  name?: string | null;
  description?: string | null;
  userConfig?: IUserConfig | null;
}

export type NewContext = Omit<IContext, 'id'> & { id: null };
