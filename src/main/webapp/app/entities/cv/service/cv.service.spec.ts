import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICV } from '../cv.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../cv.test-samples';

import { CVService } from './cv.service';

const requireRestSample: ICV = {
  ...sampleWithRequiredData,
};

describe('CV Service', () => {
  let service: CVService;
  let httpMock: HttpTestingController;
  let expectedResult: ICV | ICV[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CVService);
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

    it('should create a CV', () => {
      const cV = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cV).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CV', () => {
      const cV = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cV).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CV', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CV', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CV', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCVToCollectionIfMissing', () => {
      it('should add a CV to an empty array', () => {
        const cV: ICV = sampleWithRequiredData;
        expectedResult = service.addCVToCollectionIfMissing([], cV);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cV);
      });

      it('should not add a CV to an array that contains it', () => {
        const cV: ICV = sampleWithRequiredData;
        const cVCollection: ICV[] = [
          {
            ...cV,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCVToCollectionIfMissing(cVCollection, cV);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CV to an array that doesn't contain it", () => {
        const cV: ICV = sampleWithRequiredData;
        const cVCollection: ICV[] = [sampleWithPartialData];
        expectedResult = service.addCVToCollectionIfMissing(cVCollection, cV);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cV);
      });

      it('should add only unique CV to an array', () => {
        const cVArray: ICV[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cVCollection: ICV[] = [sampleWithRequiredData];
        expectedResult = service.addCVToCollectionIfMissing(cVCollection, ...cVArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cV: ICV = sampleWithRequiredData;
        const cV2: ICV = sampleWithPartialData;
        expectedResult = service.addCVToCollectionIfMissing([], cV, cV2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cV);
        expect(expectedResult).toContain(cV2);
      });

      it('should accept null and undefined values', () => {
        const cV: ICV = sampleWithRequiredData;
        expectedResult = service.addCVToCollectionIfMissing([], null, cV, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cV);
      });

      it('should return initial array if no CV is added', () => {
        const cVCollection: ICV[] = [sampleWithRequiredData];
        expectedResult = service.addCVToCollectionIfMissing(cVCollection, undefined, null);
        expect(expectedResult).toEqual(cVCollection);
      });
    });

    describe('compareCV', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCV(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCV(entity1, entity2);
        const compareResult2 = service.compareCV(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCV(entity1, entity2);
        const compareResult2 = service.compareCV(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCV(entity1, entity2);
        const compareResult2 = service.compareCV(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
