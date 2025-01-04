import { IDocument, NewDocument } from './document.model';

export const sampleWithRequiredData: IDocument = {
  id: 10532,
};

export const sampleWithPartialData: IDocument = {
  id: 11634,
};

export const sampleWithFullData: IDocument = {
  id: 26949,
  title: 'oh',
  content: 'how lest even',
  documentType: 'WORD',
};

export const sampleWithNewData: NewDocument = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
