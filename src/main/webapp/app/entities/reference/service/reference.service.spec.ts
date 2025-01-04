import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IReference } from '../reference.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../reference.test-samples';

import { ReferenceService } from './reference.service';

const requireRestSample: IReference = {
  ...sampleWithRequiredData,
};

describe('Reference Service', () => {
  let service: ReferenceService;
  let httpMock: HttpTestingController;
  let expectedResult: IReference | IReference[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReferenceService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Reference', () => {
      const reference = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(reference).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Reference', () => {
      const reference = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(reference).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Reference', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Reference', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Reference', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addReferenceToCollectionIfMissing', () => {
      it('should add a Reference to an empty array', () => {
        const reference: IReference = sampleWithRequiredData;
        expectedResult = service.addReferenceToCollectionIfMissing([], reference);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reference);
      });

      it('should not add a Reference to an array that contains it', () => {
        const reference: IReference = sampleWithRequiredData;
        const referenceCollection: IReference[] = [
          {
            ...reference,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReferenceToCollectionIfMissing(referenceCollection, reference);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Reference to an array that doesn't contain it", () => {
        const reference: IReference = sampleWithRequiredData;
        const referenceCollection: IReference[] = [sampleWithPartialData];
        expectedResult = service.addReferenceToCollectionIfMissing(referenceCollection, reference);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reference);
      });

      it('should add only unique Reference to an array', () => {
        const referenceArray: IReference[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const referenceCollection: IReference[] = [sampleWithRequiredData];
        expectedResult = service.addReferenceToCollectionIfMissing(referenceCollection, ...referenceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reference: IReference = sampleWithRequiredData;
        const reference2: IReference = sampleWithPartialData;
        expectedResult = service.addReferenceToCollectionIfMissing([], reference, reference2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reference);
        expect(expectedResult).toContain(reference2);
      });

      it('should accept null and undefined values', () => {
        const reference: IReference = sampleWithRequiredData;
        expectedResult = service.addReferenceToCollectionIfMissing([], null, reference, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reference);
      });

      it('should return initial array if no Reference is added', () => {
        const referenceCollection: IReference[] = [sampleWithRequiredData];
        expectedResult = service.addReferenceToCollectionIfMissing(referenceCollection, undefined, null);
        expect(expectedResult).toEqual(referenceCollection);
      });
    });

    describe('compareReference', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReference(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareReference(entity1, entity2);
        const compareResult2 = service.compareReference(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareReference(entity1, entity2);
        const compareResult2 = service.compareReference(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareReference(entity1, entity2);
        const compareResult2 = service.compareReference(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
