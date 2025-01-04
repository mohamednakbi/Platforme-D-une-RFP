import { IRole } from 'app/entities/role/role.model';
import { ITechnology } from 'app/entities/technology/technology.model';

export interface IUserConfig {
  id: number;
  userId?: string | null;
  email?: string | null;
  firstname?: string | null;
  lastname?: string | null;
  username?: string | null;
  password?: string | null;
  role?: IRole | null;

  technologys?: Pick<ITechnology, 'id'>[] | null;
}

export type NewUserConfig = Omit<IUserConfig, 'id'> & { id: null };
