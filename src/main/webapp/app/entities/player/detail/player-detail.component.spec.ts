import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PlayerDetailComponent } from './player-detail.component';

describe('Player Management Detail Component', () => {
  let comp: PlayerDetailComponent;
  let fixture: ComponentFixture<PlayerDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlayerDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./player-detail.component').then(m => m.PlayerDetailComponent),
              resolve: { player: () => of({ id: 13390 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PlayerDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PlayerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load player on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PlayerDetailComponent);

      // THEN
      expect(instance.player()).toEqual(expect.objectContaining({ id: 13390 }));
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
