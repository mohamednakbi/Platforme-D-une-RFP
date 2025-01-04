import { IUserConfig } from 'app/entities/user-config/user-config.model';

export interface IReference {
  id: number;
  title?: string | null;
  content?: string | null;
  lastmodified?: string | null;
  userConfig?: IUserConfig | null;
}

export type NewReference = Omit<IReference, 'id'> & { id: null };
