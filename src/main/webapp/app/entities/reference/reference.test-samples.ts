import { IReference, NewReference } from './reference.model';

export const sampleWithRequiredData: IReference = {
  id: 3723,
};

export const sampleWithPartialData: IReference = {
  id: 14457,
  content: 'that romance underperform',
};

export const sampleWithFullData: IReference = {
  id: 23855,
  title: 'molecule',
  content: 'miserably reduce',
  lastmodified: 'unless',
};

export const sampleWithNewData: NewReference = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
