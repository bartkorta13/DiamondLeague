import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { GameTeamDetailComponent } from './game-team-detail.component';

describe('GameTeam Management Detail Component', () => {
  let comp: GameTeamDetailComponent;
  let fixture: ComponentFixture<GameTeamDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GameTeamDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./game-team-detail.component').then(m => m.GameTeamDetailComponent),
              resolve: { gameTeam: () => of({ id: 31247 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(GameTeamDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GameTeamDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load gameTeam on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', GameTeamDetailComponent);

      // THEN
      expect(instance.gameTeam()).toEqual(expect.objectContaining({ id: 31247 }));
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
