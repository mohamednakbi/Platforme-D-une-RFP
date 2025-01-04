import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../user-config.test-samples';

import { UserConfigFormService } from './user-config-form.service';

describe('UserConfig Form Service', () => {
  let service: UserConfigFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserConfigFormService);
  });

  describe('Service methods', () => {
    describe('createUserConfigFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserConfigFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            //userId: expect.any(Object),
            email: expect.any(Object),
            firstname: expect.any(Object),
            lastname: expect.any(Object),
            username: expect.any(Object),
            password: expect.any(Object),
            role: expect.any(Object),
            technologys: expect.any(Object),
          }),
        );
      });

      it('passing IUserConfig should create a new form with FormGroup', () => {
        const formGroup = service.createUserConfigFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            // userId: expect.any(Object),
            email: expect.any(Object),
            firstname: expect.any(Object),
            lastname: expect.any(Object),
            username: expect.any(Object),
            password: expect.any(Object),
            role: expect.any(Object),
            technologys: expect.any(Object),
          }),
        );
      });
    });

    describe('getUserConfig', () => {
      it('should return NewUserConfig for default UserConfig initial value', () => {
        const formGroup = service.createUserConfigFormGroup(sampleWithNewData);

        const userConfig = service.getUserConfig(formGroup) as any;

        expect(userConfig).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserConfig for empty UserConfig initial value', () => {
        const formGroup = service.createUserConfigFormGroup();

        const userConfig = service.getUserConfig(formGroup) as any;

        expect(userConfig).toMatchObject({});
      });

      it('should return IUserConfig', () => {
        const formGroup = service.createUserConfigFormGroup(sampleWithRequiredData);

        const userConfig = service.getUserConfig(formGroup) as any;

        expect(userConfig).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserConfig should not enable id FormControl', () => {
        const formGroup = service.createUserConfigFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserConfig should disable id FormControl', () => {
        const formGroup = service.createUserConfigFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
