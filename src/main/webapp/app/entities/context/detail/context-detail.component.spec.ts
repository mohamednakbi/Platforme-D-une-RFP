import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ContextDetailComponent } from './context-detail.component';

describe('Context Management Detail Component', () => {
  let comp: ContextDetailComponent;
  let fixture: ComponentFixture<ContextDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContextDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ContextDetailComponent,
              resolve: { context: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ContextDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ContextDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load context on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ContextDetailComponent);

      // THEN
      expect(instance.context()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
