import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../context.test-samples';

import { ContextFormService } from './context-form.service';

describe('Context Form Service', () => {
  let service: ContextFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ContextFormService);
  });

  describe('Service methods', () => {
    describe('createContextFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createContextFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            userConfig: expect.any(Object),
          }),
        );
      });

      it('passing IContext should create a new form with FormGroup', () => {
        const formGroup = service.createContextFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            userConfig: expect.any(Object),
          }),
        );
      });
    });

    describe('getContext', () => {
      it('should return NewContext for default Context initial value', () => {
        const formGroup = service.createContextFormGroup(sampleWithNewData);

        const context = service.getContext(formGroup) as any;

        expect(context).toMatchObject(sampleWithNewData);
      });

      it('should return NewContext for empty Context initial value', () => {
        const formGroup = service.createContextFormGroup();

        const context = service.getContext(formGroup) as any;

        expect(context).toMatchObject({});
      });

      it('should return IContext', () => {
        const formGroup = service.createContextFormGroup(sampleWithRequiredData);

        const context = service.getContext(formGroup) as any;

        expect(context).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IContext should not enable id FormControl', () => {
        const formGroup = service.createContextFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewContext should disable id FormControl', () => {
        const formGroup = service.createContextFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
