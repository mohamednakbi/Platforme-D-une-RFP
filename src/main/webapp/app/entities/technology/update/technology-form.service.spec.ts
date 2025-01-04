import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../technology.test-samples';

import { TechnologyFormService } from './technology-form.service';

describe('Technology Form Service', () => {
  let service: TechnologyFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TechnologyFormService);
  });

  describe('Service methods', () => {
    describe('createTechnologyFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTechnologyFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            version: expect.any(Object),
            userConfigs: expect.any(Object),
          }),
        );
      });

      it('passing ITechnology should create a new form with FormGroup', () => {
        const formGroup = service.createTechnologyFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            version: expect.any(Object),
            userConfigs: expect.any(Object),
          }),
        );
      });
    });

    describe('getTechnology', () => {
      it('should return NewTechnology for default Technology initial value', () => {
        const formGroup = service.createTechnologyFormGroup(sampleWithNewData);

        const technology = service.getTechnology(formGroup) as any;

        expect(technology).toMatchObject(sampleWithNewData);
      });

      it('should return NewTechnology for empty Technology initial value', () => {
        const formGroup = service.createTechnologyFormGroup();

        const technology = service.getTechnology(formGroup) as any;

        expect(technology).toMatchObject({});
      });

      it('should return ITechnology', () => {
        const formGroup = service.createTechnologyFormGroup(sampleWithRequiredData);

        const technology = service.getTechnology(formGroup) as any;

        expect(technology).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITechnology should not enable id FormControl', () => {
        const formGroup = service.createTechnologyFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTechnology should disable id FormControl', () => {
        const formGroup = service.createTechnologyFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
