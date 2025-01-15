import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { StadiumDetailComponent } from './stadium-detail.component';

describe('Stadium Management Detail Component', () => {
  let comp: StadiumDetailComponent;
  let fixture: ComponentFixture<StadiumDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StadiumDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./stadium-detail.component').then(m => m.StadiumDetailComponent),
              resolve: { stadium: () => of({ id: 27995 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(StadiumDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StadiumDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load stadium on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', StadiumDetailComponent);

      // THEN
      expect(instance.stadium()).toEqual(expect.objectContaining({ id: 27995 }));
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
