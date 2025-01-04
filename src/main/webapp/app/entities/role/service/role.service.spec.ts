import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRole } from '../role.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../role.test-samples';

import { RoleService } from './role.service';

const requireRestSample: IRole = {
  ...sampleWithRequiredData,
};

describe('Role Service', () => {
  let service: RoleService;
  let httpMock: HttpTestingController;
  let expectedResult: IRole | IRole[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RoleService);
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

    it('should create a Role', () => {
      const role = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(role).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Role', () => {
      const role = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(role).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Role', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Role', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Role', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRoleToCollectionIfMissing', () => {
      it('should add a Role to an empty array', () => {
        const role: IRole = sampleWithRequiredData;
        expectedResult = service.addRoleToCollectionIfMissing([], role);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(role);
      });

      it('should not add a Role to an array that contains it', () => {
        const role: IRole = sampleWithRequiredData;
        const roleCollection: IRole[] = [
          {
            ...role,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRoleToCollectionIfMissing(roleCollection, role);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Role to an array that doesn't contain it", () => {
        const role: IRole = sampleWithRequiredData;
        const roleCollection: IRole[] = [sampleWithPartialData];
        expectedResult = service.addRoleToCollectionIfMissing(roleCollection, role);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(role);
      });

      it('should add only unique Role to an array', () => {
        const roleArray: IRole[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const roleCollection: IRole[] = [sampleWithRequiredData];
        expectedResult = service.addRoleToCollectionIfMissing(roleCollection, ...roleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const role: IRole = sampleWithRequiredData;
        const role2: IRole = sampleWithPartialData;
        expectedResult = service.addRoleToCollectionIfMissing([], role, role2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(role);
        expect(expectedResult).toContain(role2);
      });

      it('should accept null and undefined values', () => {
        const role: IRole = sampleWithRequiredData;
        expectedResult = service.addRoleToCollectionIfMissing([], null, role, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(role);
      });

      it('should return initial array if no Role is added', () => {
        const roleCollection: IRole[] = [sampleWithRequiredData];
        expectedResult = service.addRoleToCollectionIfMissing(roleCollection, undefined, null);
        expect(expectedResult).toEqual(roleCollection);
      });
    });

    describe('compareRole', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRole(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRole(entity1, entity2);
        const compareResult2 = service.compareRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRole(entity1, entity2);
        const compareResult2 = service.compareRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRole(entity1, entity2);
        const compareResult2 = service.compareRole(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
