import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../role.test-samples';

import { RoleFormService } from './role-form.service';

describe('Role Form Service', () => {
  let service: RoleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RoleFormService);
  });

  describe('Service methods', () => {
    describe('createRoleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRoleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            groupId: expect.any(Object),
            name: expect.any(Object),
            permission: expect.any(Object),
          }),
        );
      });

      it('passing IRole should create a new form with FormGroup', () => {
        const formGroup = service.createRoleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            groupId: expect.any(Object),
            name: expect.any(Object),
            permission: expect.any(Object),
          }),
        );
      });
    });

    describe('getRole', () => {
      it('should return NewRole for default Role initial value', () => {
        const formGroup = service.createRoleFormGroup(sampleWithNewData);

        const role = service.getRole(formGroup) as any;

        expect(role).toMatchObject(sampleWithNewData);
      });

      it('should return NewRole for empty Role initial value', () => {
        const formGroup = service.createRoleFormGroup();

        const role = service.getRole(formGroup) as any;

        expect(role).toMatchObject({});
      });

      it('should return IRole', () => {
        const formGroup = service.createRoleFormGroup(sampleWithRequiredData);

        const role = service.getRole(formGroup) as any;

        expect(role).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRole should not enable id FormControl', () => {
        const formGroup = service.createRoleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRole should disable id FormControl', () => {
        const formGroup = service.createRoleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
