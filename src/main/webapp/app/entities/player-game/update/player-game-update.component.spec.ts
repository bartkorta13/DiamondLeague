import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';
import { IGameTeam } from 'app/entities/game-team/game-team.model';
import { GameTeamService } from 'app/entities/game-team/service/game-team.service';
import { IPlayerGame } from '../player-game.model';
import { PlayerGameService } from '../service/player-game.service';
import { PlayerGameFormService } from './player-game-form.service';

import { PlayerGameUpdateComponent } from './player-game-update.component';

describe('PlayerGame Management Update Component', () => {
  let comp: PlayerGameUpdateComponent;
  let fixture: ComponentFixture<PlayerGameUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let playerGameFormService: PlayerGameFormService;
  let playerGameService: PlayerGameService;
  let playerService: PlayerService;
  let gameTeamService: GameTeamService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PlayerGameUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PlayerGameUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlayerGameUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    playerGameFormService = TestBed.inject(PlayerGameFormService);
    playerGameService = TestBed.inject(PlayerGameService);
    playerService = TestBed.inject(PlayerService);
    gameTeamService = TestBed.inject(GameTeamService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Player query and add missing value', () => {
      const playerGame: IPlayerGame = { id: 10798 };
      const player: IPlayer = { id: 13390 };
      playerGame.player = player;

      const playerCollection: IPlayer[] = [{ id: 13390 }];
      jest.spyOn(playerService, 'query').mockReturnValue(of(new HttpResponse({ body: playerCollection })));
      const additionalPlayers = [player];
      const expectedCollection: IPlayer[] = [...additionalPlayers, ...playerCollection];
      jest.spyOn(playerService, 'addPlayerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ playerGame });
      comp.ngOnInit();

      expect(playerService.query).toHaveBeenCalled();
      expect(playerService.addPlayerToCollectionIfMissing).toHaveBeenCalledWith(
        playerCollection,
        ...additionalPlayers.map(expect.objectContaining),
      );
      expect(comp.playersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call GameTeam query and add missing value', () => {
      const playerGame: IPlayerGame = { id: 10798 };
      const gameTeam: IGameTeam = { id: 31247 };
      playerGame.gameTeam = gameTeam;

      const gameTeamCollection: IGameTeam[] = [{ id: 31247 }];
      jest.spyOn(gameTeamService, 'query').mockReturnValue(of(new HttpResponse({ body: gameTeamCollection })));
      const additionalGameTeams = [gameTeam];
      const expectedCollection: IGameTeam[] = [...additionalGameTeams, ...gameTeamCollection];
      jest.spyOn(gameTeamService, 'addGameTeamToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ playerGame });
      comp.ngOnInit();

      expect(gameTeamService.query).toHaveBeenCalled();
      expect(gameTeamService.addGameTeamToCollectionIfMissing).toHaveBeenCalledWith(
        gameTeamCollection,
        ...additionalGameTeams.map(expect.objectContaining),
      );
      expect(comp.gameTeamsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const playerGame: IPlayerGame = { id: 10798 };
      const player: IPlayer = { id: 13390 };
      playerGame.player = player;
      const gameTeam: IGameTeam = { id: 31247 };
      playerGame.gameTeam = gameTeam;

      activatedRoute.data = of({ playerGame });
      comp.ngOnInit();

      expect(comp.playersSharedCollection).toContainEqual(player);
      expect(comp.gameTeamsSharedCollection).toContainEqual(gameTeam);
      expect(comp.playerGame).toEqual(playerGame);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlayerGame>>();
      const playerGame = { id: 23206 };
      jest.spyOn(playerGameFormService, 'getPlayerGame').mockReturnValue(playerGame);
      jest.spyOn(playerGameService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playerGame });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: playerGame }));
      saveSubject.complete();

      // THEN
      expect(playerGameFormService.getPlayerGame).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(playerGameService.update).toHaveBeenCalledWith(expect.objectContaining(playerGame));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlayerGame>>();
      const playerGame = { id: 23206 };
      jest.spyOn(playerGameFormService, 'getPlayerGame').mockReturnValue({ id: null });
      jest.spyOn(playerGameService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playerGame: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: playerGame }));
      saveSubject.complete();

      // THEN
      expect(playerGameFormService.getPlayerGame).toHaveBeenCalled();
      expect(playerGameService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlayerGame>>();
      const playerGame = { id: 23206 };
      jest.spyOn(playerGameService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playerGame });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(playerGameService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePlayer', () => {
      it('Should forward to playerService', () => {
        const entity = { id: 13390 };
        const entity2 = { id: 32153 };
        jest.spyOn(playerService, 'comparePlayer');
        comp.comparePlayer(entity, entity2);
        expect(playerService.comparePlayer).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareGameTeam', () => {
      it('Should forward to gameTeamService', () => {
        const entity = { id: 31247 };
        const entity2 = { id: 28924 };
        jest.spyOn(gameTeamService, 'compareGameTeam');
        comp.compareGameTeam(entity, entity2);
        expect(gameTeamService.compareGameTeam).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
