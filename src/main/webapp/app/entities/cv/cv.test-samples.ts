import { ICV, NewCV } from './cv.model';

export const sampleWithRequiredData: ICV = {
  id: 3021,
};

export const sampleWithPartialData: ICV = {
  id: 4113,
  content: 'whereas propane via',
};

export const sampleWithFullData: ICV = {
  id: 24041,
  title: 'within recoup',
  content: 'likely whoa popular',
};

export const sampleWithNewData: NewCV = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
