import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IUserConfig } from 'app/entities/user-config/user-config.model';
import { UserConfigService } from 'app/entities/user-config/service/user-config.service';
import { TechnologyService } from '../service/technology.service';
import { ITechnology } from '../technology.model';
import { TechnologyFormService } from './technology-form.service';

import { TechnologyUpdateComponent } from './technology-update.component';

describe('Technology Management Update Component', () => {
  let comp: TechnologyUpdateComponent;
  let fixture: ComponentFixture<TechnologyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let technologyFormService: TechnologyFormService;
  let technologyService: TechnologyService;
  let userConfigService: UserConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TechnologyUpdateComponent],
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
      .overrideTemplate(TechnologyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TechnologyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    technologyFormService = TestBed.inject(TechnologyFormService);
    technologyService = TestBed.inject(TechnologyService);
    userConfigService = TestBed.inject(UserConfigService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call UserConfig query and add missing value', () => {
      const technology: ITechnology = { id: 456 };
      const userConfigs: IUserConfig[] = [{ id: 32095 }];
      technology.userConfigs = userConfigs;

      const userConfigCollection: IUserConfig[] = [{ id: 3068 }];
      jest.spyOn(userConfigService, 'query').mockReturnValue(of(new HttpResponse({ body: userConfigCollection })));
      const additionalUserConfigs = [...userConfigs];
      const expectedCollection: IUserConfig[] = [...additionalUserConfigs, ...userConfigCollection];
      jest.spyOn(userConfigService, 'addUserConfigToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ technology });
      comp.ngOnInit();

      expect(userConfigService.query).toHaveBeenCalled();
      expect(userConfigService.addUserConfigToCollectionIfMissing).toHaveBeenCalledWith(
        userConfigCollection,
        ...additionalUserConfigs.map(expect.objectContaining),
      );
      expect(comp.userConfigsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const technology: ITechnology = { id: 456 };
      const userConfigs: IUserConfig = { id: 17755 };
      technology.userConfigs = [userConfigs];

      activatedRoute.data = of({ technology });
      comp.ngOnInit();

      expect(comp.userConfigsSharedCollection).toContain(userConfigs);
      expect(comp.technology).toEqual(technology);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechnology>>();
      const technology = { id: 123 };
      jest.spyOn(technologyFormService, 'getTechnology').mockReturnValue(technology);
      jest.spyOn(technologyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ technology });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: technology }));
      saveSubject.complete();

      // THEN
      expect(technologyFormService.getTechnology).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(technologyService.update).toHaveBeenCalledWith(expect.objectContaining(technology));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechnology>>();
      const technology = { id: 123 };
      jest.spyOn(technologyFormService, 'getTechnology').mockReturnValue({ id: null });
      jest.spyOn(technologyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ technology: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: technology }));
      saveSubject.complete();

      // THEN
      expect(technologyFormService.getTechnology).toHaveBeenCalled();
      expect(technologyService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechnology>>();
      const technology = { id: 123 };
      jest.spyOn(technologyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ technology });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(technologyService.update).toHaveBeenCalled();
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
