import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IUserConfig } from 'app/entities/user-config/user-config.model';
import { UserConfigService } from 'app/entities/user-config/service/user-config.service';
import { ReferenceService } from '../service/reference.service';
import { IReference } from '../reference.model';
import { ReferenceFormService } from './reference-form.service';

import { ReferenceUpdateComponent } from './reference-update.component';

describe('Reference Management Update Component', () => {
  let comp: ReferenceUpdateComponent;
  let fixture: ComponentFixture<ReferenceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let referenceFormService: ReferenceFormService;
  let referenceService: ReferenceService;
  let userConfigService: UserConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ReferenceUpdateComponent],
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
      .overrideTemplate(ReferenceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReferenceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    referenceFormService = TestBed.inject(ReferenceFormService);
    referenceService = TestBed.inject(ReferenceService);
    userConfigService = TestBed.inject(UserConfigService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call UserConfig query and add missing value', () => {
      const reference: IReference = { id: 456 };
      const userConfig: IUserConfig = { id: 10905 };
      reference.userConfig = userConfig;

      const userConfigCollection: IUserConfig[] = [{ id: 23812 }];
      jest.spyOn(userConfigService, 'query').mockReturnValue(of(new HttpResponse({ body: userConfigCollection })));
      const additionalUserConfigs = [userConfig];
      const expectedCollection: IUserConfig[] = [...additionalUserConfigs, ...userConfigCollection];
      jest.spyOn(userConfigService, 'addUserConfigToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reference });
      comp.ngOnInit();

      expect(userConfigService.query).toHaveBeenCalled();
      expect(userConfigService.addUserConfigToCollectionIfMissing).toHaveBeenCalledWith(
        userConfigCollection,
        ...additionalUserConfigs.map(expect.objectContaining),
      );
      expect(comp.userConfigsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const reference: IReference = { id: 456 };
      const userConfig: IUserConfig = { id: 2244 };
      reference.userConfig = userConfig;

      activatedRoute.data = of({ reference });
      comp.ngOnInit();

      expect(comp.userConfigsSharedCollection).toContain(userConfig);
      expect(comp.reference).toEqual(reference);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReference>>();
      const reference = { id: 123 };
      jest.spyOn(referenceFormService, 'getReference').mockReturnValue(reference);
      jest.spyOn(referenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reference });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reference }));
      saveSubject.complete();

      // THEN
      expect(referenceFormService.getReference).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(referenceService.update).toHaveBeenCalledWith(expect.objectContaining(reference));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReference>>();
      const reference = { id: 123 };
      jest.spyOn(referenceFormService, 'getReference').mockReturnValue({ id: null });
      jest.spyOn(referenceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reference: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reference }));
      saveSubject.complete();

      // THEN
      expect(referenceFormService.getReference).toHaveBeenCalled();
      expect(referenceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReference>>();
      const reference = { id: 123 };
      jest.spyOn(referenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reference });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(referenceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUserConfig', () => {
      it('Should forward to userConfigService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userConfigService, 'compareUserConfig');
        comp.compareUserConfig(entity, entity2);
        expect(userConfigService.compareUserConfig).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
