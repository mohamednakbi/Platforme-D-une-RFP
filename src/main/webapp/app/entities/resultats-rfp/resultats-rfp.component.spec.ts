import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResultatsRFPComponent } from './resultats-rfp.component';

describe('ResultatsRFPComponent', () => {
  let component: ResultatsRFPComponent;
  let fixture: ComponentFixture<ResultatsRFPComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResultatsRFPComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ResultatsRFPComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
