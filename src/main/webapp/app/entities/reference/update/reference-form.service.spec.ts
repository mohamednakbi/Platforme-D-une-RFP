import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../reference.test-samples';

import { ReferenceFormService } from './reference-form.service';

describe('Reference Form Service', () => {
  let service: ReferenceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReferenceFormService);
  });

  describe('Service methods', () => {
    describe('createReferenceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReferenceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            content: expect.any(Object),
            lastmodified: expect.any(Object),
            userConfig: expect.any(Object),
          }),
        );
      });

      it('passing IReference should create a new form with FormGroup', () => {
        const formGroup = service.createReferenceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            content: expect.any(Object),
            lastmodified: expect.any(Object),
            userConfig: expect.any(Object),
          }),
        );
      });
    });

    describe('getReference', () => {
      it('should return NewReference for default Reference initial value', () => {
        const formGroup = service.createReferenceFormGroup(sampleWithNewData);

        const reference = service.getReference(formGroup) as any;

        expect(reference).toMatchObject(sampleWithNewData);
      });

      it('should return NewReference for empty Reference initial value', () => {
        const formGroup = service.createReferenceFormGroup();

        const reference = service.getReference(formGroup) as any;

        expect(reference).toMatchObject({});
      });

      it('should return IReference', () => {
        const formGroup = service.createReferenceFormGroup(sampleWithRequiredData);

        const reference = service.getReference(formGroup) as any;

        expect(reference).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReference should not enable id FormControl', () => {
        const formGroup = service.createReferenceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReference should disable id FormControl', () => {
        const formGroup = service.createReferenceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
