import { IRole, NewRole } from './role.model';

export const sampleWithRequiredData: IRole = {
  id: 25690,
};

export const sampleWithPartialData: IRole = {
  id: 31993,
  groupId: '08c39828-6b72-4ae3-abe9-dea10614e5c4',
  name: 'spanish',
  permission: false,
};

export const sampleWithFullData: IRole = {
  id: 19237,
  groupId: '353e8255-5d48-4980-ad77-5dbc2d4b0c95',
  name: 'spike',
  permission: false,
};

export const sampleWithNewData: NewRole = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
