import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IUserConfig } from 'app/entities/user-config/user-config.model';
import { UserConfigService } from 'app/entities/user-config/service/user-config.service';
import { CVService } from '../service/cv.service';
import { ICV } from '../cv.model';
import { CVFormService } from './cv-form.service';

import { CVUpdateComponent } from './cv-update.component';

describe('CV Management Update Component', () => {
  let comp: CVUpdateComponent;
  let fixture: ComponentFixture<CVUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cVFormService: CVFormService;
  let cVService: CVService;
  let userConfigService: UserConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, CVUpdateComponent],
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
      .overrideTemplate(CVUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CVUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cVFormService = TestBed.inject(CVFormService);
    cVService = TestBed.inject(CVService);
    userConfigService = TestBed.inject(UserConfigService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call userConfig query and add missing value', () => {
      const cV: ICV = { id: 456 };
      const userConfig: IUserConfig = { id: 1436 };
      cV.userConfig = userConfig;

      const userConfigCollection: IUserConfig[] = [{ id: 11308 }];
      jest.spyOn(userConfigService, 'query').mockReturnValue(of(new HttpResponse({ body: userConfigCollection })));
      const expectedCollection: IUserConfig[] = [userConfig, ...userConfigCollection];
      jest.spyOn(userConfigService, 'addUserConfigToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cV });
      comp.ngOnInit();

      expect(userConfigService.query).toHaveBeenCalled();
      expect(userConfigService.addUserConfigToCollectionIfMissing).toHaveBeenCalledWith(userConfigCollection, userConfig);
      expect(comp.userConfigsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cV: ICV = { id: 456 };
      const userConfig: IUserConfig = { id: 7681 };
      cV.userConfig = userConfig;

      activatedRoute.data = of({ cV });
      comp.ngOnInit();

      expect(comp.userConfigsCollection).toContain(userConfig);
      expect(comp.cV).toEqual(cV);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICV>>();
      const cV = { id: 123 };
      jest.spyOn(cVFormService, 'getCV').mockReturnValue(cV);
      jest.spyOn(cVService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cV });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cV }));
      saveSubject.complete();

      // THEN
      expect(cVFormService.getCV).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cVService.update).toHaveBeenCalledWith(expect.objectContaining(cV));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICV>>();
      const cV = { id: 123 };
      jest.spyOn(cVFormService, 'getCV').mockReturnValue({ id: null });
      jest.spyOn(cVService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cV: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cV }));
      saveSubject.complete();

      // THEN
      expect(cVFormService.getCV).toHaveBeenCalled();
      expect(cVService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICV>>();
      const cV = { id: 123 };
      jest.spyOn(cVService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cV });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cVService.update).toHaveBeenCalled();
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
