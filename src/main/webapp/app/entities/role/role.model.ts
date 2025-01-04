export interface IRole {
  id: number;
  groupId?: string | null;
  name?: string | null;
  permission?: boolean | null;
  authorities?: string[] | null;
}

export type NewRole = Omit<IRole, 'id'> & { id: null };
