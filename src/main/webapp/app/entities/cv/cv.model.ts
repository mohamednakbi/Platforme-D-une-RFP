import { IUserConfig } from 'app/entities/user-config/user-config.model';

export interface ICV {
  id: number;
  title?: string | null;
  content?: string | null;
  userConfig?: Pick<IUserConfig, 'id' | 'username'> | null;
}

export type NewCV = Omit<ICV, 'id'> & { id: null };
