import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';
import { IGame } from 'app/entities/game/game.model';
import { GameService } from 'app/entities/game/service/game.service';
import { IGameTeam } from '../game-team.model';
import { GameTeamService } from '../service/game-team.service';
import { GameTeamFormService } from './game-team-form.service';

import { GameTeamUpdateComponent } from './game-team-update.component';

describe('GameTeam Management Update Component', () => {
  let comp: GameTeamUpdateComponent;
  let fixture: ComponentFixture<GameTeamUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gameTeamFormService: GameTeamFormService;
  let gameTeamService: GameTeamService;
  let playerService: PlayerService;
  let gameService: GameService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [GameTeamUpdateComponent],
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
      .overrideTemplate(GameTeamUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GameTeamUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    gameTeamFormService = TestBed.inject(GameTeamFormService);
    gameTeamService = TestBed.inject(GameTeamService);
    playerService = TestBed.inject(PlayerService);
    gameService = TestBed.inject(GameService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Player query and add missing value', () => {
      const gameTeam: IGameTeam = { id: 28924 };
      const captain: IPlayer = { id: 13390 };
      gameTeam.captain = captain;

      const playerCollection: IPlayer[] = [{ id: 13390 }];
      jest.spyOn(playerService, 'query').mockReturnValue(of(new HttpResponse({ body: playerCollection })));
      const additionalPlayers = [captain];
      const expectedCollection: IPlayer[] = [...additionalPlayers, ...playerCollection];
      jest.spyOn(playerService, 'addPlayerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ gameTeam });
      comp.ngOnInit();

      expect(playerService.query).toHaveBeenCalled();
      expect(playerService.addPlayerToCollectionIfMissing).toHaveBeenCalledWith(
        playerCollection,
        ...additionalPlayers.map(expect.objectContaining),
      );
      expect(comp.playersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Game query and add missing value', () => {
      const gameTeam: IGameTeam = { id: 28924 };
      const game: IGame = { id: 7137 };
      gameTeam.game = game;

      const gameCollection: IGame[] = [{ id: 7137 }];
      jest.spyOn(gameService, 'query').mockReturnValue(of(new HttpResponse({ body: gameCollection })));
      const additionalGames = [game];
      const expectedCollection: IGame[] = [...additionalGames, ...gameCollection];
      jest.spyOn(gameService, 'addGameToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ gameTeam });
      comp.ngOnInit();

      expect(gameService.query).toHaveBeenCalled();
      expect(gameService.addGameToCollectionIfMissing).toHaveBeenCalledWith(
        gameCollection,
        ...additionalGames.map(expect.objectContaining),
      );
      expect(comp.gamesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const gameTeam: IGameTeam = { id: 28924 };
      const captain: IPlayer = { id: 13390 };
      gameTeam.captain = captain;
      const game: IGame = { id: 7137 };
      gameTeam.game = game;

      activatedRoute.data = of({ gameTeam });
      comp.ngOnInit();

      expect(comp.playersSharedCollection).toContainEqual(captain);
      expect(comp.gamesSharedCollection).toContainEqual(game);
      expect(comp.gameTeam).toEqual(gameTeam);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameTeam>>();
      const gameTeam = { id: 31247 };
      jest.spyOn(gameTeamFormService, 'getGameTeam').mockReturnValue(gameTeam);
      jest.spyOn(gameTeamService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameTeam });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gameTeam }));
      saveSubject.complete();

      // THEN
      expect(gameTeamFormService.getGameTeam).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(gameTeamService.update).toHaveBeenCalledWith(expect.objectContaining(gameTeam));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameTeam>>();
      const gameTeam = { id: 31247 };
      jest.spyOn(gameTeamFormService, 'getGameTeam').mockReturnValue({ id: null });
      jest.spyOn(gameTeamService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameTeam: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gameTeam }));
      saveSubject.complete();

      // THEN
      expect(gameTeamFormService.getGameTeam).toHaveBeenCalled();
      expect(gameTeamService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameTeam>>();
      const gameTeam = { id: 31247 };
      jest.spyOn(gameTeamService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameTeam });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(gameTeamService.update).toHaveBeenCalled();
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

    describe('compareGame', () => {
      it('Should forward to gameService', () => {
        const entity = { id: 7137 };
        const entity2 = { id: 5760 };
        jest.spyOn(gameService, 'compareGame');
        comp.compareGame(entity, entity2);
        expect(gameService.compareGame).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
