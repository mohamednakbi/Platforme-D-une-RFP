import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IRole } from 'app/entities/role/role.model';
import { RoleService } from 'app/entities/role/service/role.service';
import { ITechnology } from 'app/entities/technology/technology.model';
import { TechnologyService } from 'app/entities/technology/service/technology.service';
import { IUserConfig } from '../user-config.model';
import { UserConfigService } from '../service/user-config.service';
import { UserConfigFormService } from './user-config-form.service';

import { UserConfigUpdateComponent } from './user-config-update.component';

describe('UserConfig Management Update Component', () => {
  let comp: UserConfigUpdateComponent;
  let fixture: ComponentFixture<UserConfigUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userConfigFormService: UserConfigFormService;
  let userConfigService: UserConfigService;
  let roleService: RoleService;
  let technologyService: TechnologyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, UserConfigUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(UserConfigUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserConfigUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userConfigFormService = TestBed.inject(UserConfigFormService);
    userConfigService = TestBed.inject(UserConfigService);
    roleService = TestBed.inject(RoleService);
    technologyService = TestBed.inject(TechnologyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Role query and add missing value', () => {
      const userConfig: IUserConfig = { id: 456 };
      const role: IRole = { id: 19114 };
      userConfig.role = role;

      const roleCollection: IRole[] = [{ id: 14666 }];
      jest.spyOn(roleService, 'query').mockReturnValue(of(new HttpResponse({ body: roleCollection })));
      const additionalRoles = [role];
      const expectedCollection: IRole[] = [...additionalRoles, ...roleCollection];
      jest.spyOn(roleService, 'addRoleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userConfig });
      comp.ngOnInit();

      expect(roleService.query).toHaveBeenCalled();
      expect(roleService.addRoleToCollectionIfMissing).toHaveBeenCalledWith(
        roleCollection,
        ...additionalRoles.map(expect.objectContaining),
      );
      expect(comp.rolesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Technology query and add missing value', () => {
      const userConfig: IUserConfig = { id: 456 };
      const technologys: ITechnology[] = [{ id: 158 }];
      userConfig.technologys = technologys;

      const technologyCollection: ITechnology[] = [{ id: 10195 }];
      jest.spyOn(technologyService, 'query').mockReturnValue(of(new HttpResponse({ body: technologyCollection })));
      const additionalTechnologies = [...technologys];
      const expectedCollection: ITechnology[] = [...additionalTechnologies, ...technologyCollection];
      jest.spyOn(technologyService, 'addTechnologyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userConfig });
      comp.ngOnInit();

      expect(technologyService.query).toHaveBeenCalled();
      expect(technologyService.addTechnologyToCollectionIfMissing).toHaveBeenCalledWith(
        technologyCollection,
        ...additionalTechnologies.map(expect.objectContaining),
      );
      expect(comp.technologiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userConfig: IUserConfig = { id: 456 };
      const role: IRole = { id: 11947 };
      userConfig.role = role;
      const technologys: ITechnology = { id: 16370 };
      userConfig.technologys = [technologys];

      activatedRoute.data = of({ userConfig });
      comp.ngOnInit();

      expect(comp.rolesSharedCollection).toContain(role);
      expect(comp.technologiesSharedCollection).toContain(technologys);
      expect(comp.userConfig).toEqual(userConfig);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserConfig>>();
      const userConfig = { id: 123 };
      jest.spyOn(userConfigFormService, 'getUserConfig').mockReturnValue(userConfig);
      jest.spyOn(userConfigService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userConfig });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userConfig }));
      saveSubject.complete();

      // THEN
      expect(userConfigFormService.getUserConfig).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userConfigService.update).toHaveBeenCalledWith(expect.objectContaining(userConfig));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserConfig>>();
      const userConfig = { id: 123 };
      jest.spyOn(userConfigFormService, 'getUserConfig').mockReturnValue({ id: null });
      jest.spyOn(userConfigService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userConfig: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userConfig }));
      saveSubject.complete();

      // THEN
      expect(userConfigFormService.getUserConfig).toHaveBeenCalled();
      expect(userConfigService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserConfig>>();
      const userConfig = { id: 123 };
      jest.spyOn(userConfigService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userConfig });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userConfigService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareRole', () => {
      it('Should forward to roleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(roleService, 'compareRole');
        comp.compareRole(entity, entity2);
        expect(roleService.compareRole).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTechnology', () => {
      it('Should forward to technologyService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(technologyService, 'compareTechnology');
        comp.compareTechnology(entity, entity2);
        expect(technologyService.compareTechnology).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
