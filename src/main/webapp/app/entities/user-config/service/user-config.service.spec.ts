import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUserConfig } from '../user-config.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../user-config.test-samples';

import { UserConfigService } from './user-config.service';

const requireRestSample: IUserConfig = {
  ...sampleWithRequiredData,
};

describe('UserConfig Service', () => {
  let service: UserConfigService;
  let httpMock: HttpTestingController;
  let expectedResult: IUserConfig | IUserConfig[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UserConfigService);
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

    it('should create a UserConfig', () => {
      const userConfig = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(userConfig).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserConfig', () => {
      const userConfig = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(userConfig).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserConfig', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserConfig', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UserConfig', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUserConfigToCollectionIfMissing', () => {
      it('should add a UserConfig to an empty array', () => {
        const userConfig: IUserConfig = sampleWithRequiredData;
        expectedResult = service.addUserConfigToCollectionIfMissing([], userConfig);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userConfig);
      });

      it('should not add a UserConfig to an array that contains it', () => {
        const userConfig: IUserConfig = sampleWithRequiredData;
        const userConfigCollection: IUserConfig[] = [
          {
            ...userConfig,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUserConfigToCollectionIfMissing(userConfigCollection, userConfig);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserConfig to an array that doesn't contain it", () => {
        const userConfig: IUserConfig = sampleWithRequiredData;
        const userConfigCollection: IUserConfig[] = [sampleWithPartialData];
        expectedResult = service.addUserConfigToCollectionIfMissing(userConfigCollection, userConfig);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userConfig);
      });

      it('should add only unique UserConfig to an array', () => {
        const userConfigArray: IUserConfig[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const userConfigCollection: IUserConfig[] = [sampleWithRequiredData];
        expectedResult = service.addUserConfigToCollectionIfMissing(userConfigCollection, ...userConfigArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userConfig: IUserConfig = sampleWithRequiredData;
        const userConfig2: IUserConfig = sampleWithPartialData;
        expectedResult = service.addUserConfigToCollectionIfMissing([], userConfig, userConfig2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userConfig);
        expect(expectedResult).toContain(userConfig2);
      });

      it('should accept null and undefined values', () => {
        const userConfig: IUserConfig = sampleWithRequiredData;
        expectedResult = service.addUserConfigToCollectionIfMissing([], null, userConfig, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userConfig);
      });

      it('should return initial array if no UserConfig is added', () => {
        const userConfigCollection: IUserConfig[] = [sampleWithRequiredData];
        expectedResult = service.addUserConfigToCollectionIfMissing(userConfigCollection, undefined, null);
        expect(expectedResult).toEqual(userConfigCollection);
      });
    });

    describe('compareUserConfig', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUserConfig(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUserConfig(entity1, entity2);
        const compareResult2 = service.compareUserConfig(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUserConfig(entity1, entity2);
        const compareResult2 = service.compareUserConfig(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUserConfig(entity1, entity2);
        const compareResult2 = service.compareUserConfig(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
