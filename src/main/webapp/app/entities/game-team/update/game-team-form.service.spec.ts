import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../game-team.test-samples';

import { GameTeamFormService } from './game-team-form.service';

describe('GameTeam Form Service', () => {
  let service: GameTeamFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GameTeamFormService);
  });

  describe('Service methods', () => {
    describe('createGameTeamFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGameTeamFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            goals: expect.any(Object),
            captain: expect.any(Object),
            game: expect.any(Object),
          }),
        );
      });

      it('passing IGameTeam should create a new form with FormGroup', () => {
        const formGroup = service.createGameTeamFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            goals: expect.any(Object),
            captain: expect.any(Object),
            game: expect.any(Object),
          }),
        );
      });
    });

    describe('getGameTeam', () => {
      it('should return NewGameTeam for default GameTeam initial value', () => {
        const formGroup = service.createGameTeamFormGroup(sampleWithNewData);

        const gameTeam = service.getGameTeam(formGroup) as any;

        expect(gameTeam).toMatchObject(sampleWithNewData);
      });

      it('should return NewGameTeam for empty GameTeam initial value', () => {
        const formGroup = service.createGameTeamFormGroup();

        const gameTeam = service.getGameTeam(formGroup) as any;

        expect(gameTeam).toMatchObject({});
      });

      it('should return IGameTeam', () => {
        const formGroup = service.createGameTeamFormGroup(sampleWithRequiredData);

        const gameTeam = service.getGameTeam(formGroup) as any;

        expect(gameTeam).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGameTeam should not enable id FormControl', () => {
        const formGroup = service.createGameTeamFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGameTeam should disable id FormControl', () => {
        const formGroup = service.createGameTeamFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
