import { IContext, NewContext } from './context.model';

export const sampleWithRequiredData: IContext = {
  id: 21995,
};

export const sampleWithPartialData: IContext = {
  id: 5084,
  name: 'boohoo mull given',
  description: 'jilt',
};

export const sampleWithFullData: IContext = {
  id: 3813,
  name: 'inborn march',
  description: 'pollard minus via',
};

export const sampleWithNewData: NewContext = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
