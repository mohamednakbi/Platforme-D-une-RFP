import { IUserConfig, NewUserConfig } from './user-config.model';

export const sampleWithRequiredData: IUserConfig = {
  id: 22477,
};

export const sampleWithPartialData: IUserConfig = {
  id: 30135,
  email: 'Jane_Klocko46@hotmail.com',
  username: 'hastily putrid',
  password: 'molder calculus',
};

export const sampleWithFullData: IUserConfig = {
  id: 5736,
  userId: '200222d7-9ef1-41d1-9b0f-13f1a818f909',
  email: 'Tiana.Feeney@yahoo.com',
  firstname: 'laborer stockpile',
  lastname: 'playfully luxurious',
  username: 'little chuckle warmly',
  password: 'tax bowed porch',
};

export const sampleWithNewData: NewUserConfig = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
