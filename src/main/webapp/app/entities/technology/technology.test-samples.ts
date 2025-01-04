import { ITechnology, NewTechnology } from './technology.model';

export const sampleWithRequiredData: ITechnology = {
  id: 1833,
};

export const sampleWithPartialData: ITechnology = {
  id: 28117,
  version: 'boiling',
};

export const sampleWithFullData: ITechnology = {
  id: 13341,
  name: 'necessitate throughout',
  version: 'but oof',
};

export const sampleWithNewData: NewTechnology = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
