import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IUserConfig } from 'app/entities/user-config/user-config.model';
import { UserConfigService } from 'app/entities/user-config/service/user-config.service';
import { ContextService } from '../service/context.service';
import { IContext } from '../context.model';
import { ContextFormService } from './context-form.service';

import { ContextUpdateComponent } from './context-update.component';

describe('Context Management Update Component', () => {
  let comp: ContextUpdateComponent;
  let fixture: ComponentFixture<ContextUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contextFormService: ContextFormService;
  let contextService: ContextService;
  let userConfigService: UserConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ContextUpdateComponent],
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
      .overrideTemplate(ContextUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContextUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contextFormService = TestBed.inject(ContextFormService);
    contextService = TestBed.inject(ContextService);
    userConfigService = TestBed.inject(UserConfigService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call UserConfig query and add missing value', () => {
      const context: IContext = { id: 456 };
      const userConfig: IUserConfig = { id: 31114 };
      context.userConfig = userConfig;

      const userConfigCollection: IUserConfig[] = [{ id: 18055 }];
      jest.spyOn(userConfigService, 'query').mockReturnValue(of(new HttpResponse({ body: userConfigCollection })));
      const additionalUserConfigs = [userConfig];
      const expectedCollection: IUserConfig[] = [...additionalUserConfigs, ...userConfigCollection];
      jest.spyOn(userConfigService, 'addUserConfigToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ context });
      comp.ngOnInit();

      expect(userConfigService.query).toHaveBeenCalled();
      expect(userConfigService.addUserConfigToCollectionIfMissing).toHaveBeenCalledWith(
        userConfigCollection,
        ...additionalUserConfigs.map(expect.objectContaining),
      );
      expect(comp.userConfigsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const context: IContext = { id: 456 };
      const userConfig: IUserConfig = { id: 9606 };
      context.userConfig = userConfig;

      activatedRoute.data = of({ context });
      comp.ngOnInit();

      expect(comp.userConfigsSharedCollection).toContain(userConfig);
      expect(comp.context).toEqual(context);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContext>>();
      const context = { id: 123 };
      jest.spyOn(contextFormService, 'getContext').mockReturnValue(context);
      jest.spyOn(contextService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ context });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: context }));
      saveSubject.complete();

      // THEN
      expect(contextFormService.getContext).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contextService.update).toHaveBeenCalledWith(expect.objectContaining(context));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContext>>();
      const context = { id: 123 };
      jest.spyOn(contextFormService, 'getContext').mockReturnValue({ id: null });
      jest.spyOn(contextService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ context: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: context }));
      saveSubject.complete();

      // THEN
      expect(contextFormService.getContext).toHaveBeenCalled();
      expect(contextService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContext>>();
      const context = { id: 123 };
      jest.spyOn(contextService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ context });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contextService.update).toHaveBeenCalled();
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
