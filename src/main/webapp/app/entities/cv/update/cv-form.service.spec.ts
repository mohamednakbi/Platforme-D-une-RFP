import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../cv.test-samples';

import { CVFormService } from './cv-form.service';

describe('CV Form Service', () => {
  let service: CVFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CVFormService);
  });

  describe('Service methods', () => {
    describe('createCVFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCVFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            content: expect.any(Object),
            userConfig: expect.any(Object),
          }),
        );
      });

      it('passing ICV should create a new form with FormGroup', () => {
        const formGroup = service.createCVFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            content: expect.any(Object),
            userConfig: expect.any(Object),
          }),
        );
      });
    });

    describe('getCV', () => {
      it('should return NewCV for default CV initial value', () => {
        const formGroup = service.createCVFormGroup(sampleWithNewData);

        const cV = service.getCV(formGroup) as any;

        expect(cV).toMatchObject(sampleWithNewData);
      });

      it('should return NewCV for empty CV initial value', () => {
        const formGroup = service.createCVFormGroup();

        const cV = service.getCV(formGroup) as any;

        expect(cV).toMatchObject({});
      });

      it('should return ICV', () => {
        const formGroup = service.createCVFormGroup(sampleWithRequiredData);

        const cV = service.getCV(formGroup) as any;

        expect(cV).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICV should not enable id FormControl', () => {
        const formGroup = service.createCVFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCV should disable id FormControl', () => {
        const formGroup = service.createCVFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
