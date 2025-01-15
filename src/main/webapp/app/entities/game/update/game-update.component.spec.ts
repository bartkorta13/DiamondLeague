import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IStadium } from 'app/entities/stadium/stadium.model';
import { StadiumService } from 'app/entities/stadium/service/stadium.service';
import { GameService } from '../service/game.service';
import { IGame } from '../game.model';
import { GameFormService } from './game-form.service';

import { GameUpdateComponent } from './game-update.component';

describe('Game Management Update Component', () => {
  let comp: GameUpdateComponent;
  let fixture: ComponentFixture<GameUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gameFormService: GameFormService;
  let gameService: GameService;
  let stadiumService: StadiumService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [GameUpdateComponent],
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
      .overrideTemplate(GameUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GameUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    gameFormService = TestBed.inject(GameFormService);
    gameService = TestBed.inject(GameService);
    stadiumService = TestBed.inject(StadiumService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Stadium query and add missing value', () => {
      const game: IGame = { id: 5760 };
      const stadium: IStadium = { id: 27995 };
      game.stadium = stadium;

      const stadiumCollection: IStadium[] = [{ id: 27995 }];
      jest.spyOn(stadiumService, 'query').mockReturnValue(of(new HttpResponse({ body: stadiumCollection })));
      const additionalStadiums = [stadium];
      const expectedCollection: IStadium[] = [...additionalStadiums, ...stadiumCollection];
      jest.spyOn(stadiumService, 'addStadiumToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ game });
      comp.ngOnInit();

      expect(stadiumService.query).toHaveBeenCalled();
      expect(stadiumService.addStadiumToCollectionIfMissing).toHaveBeenCalledWith(
        stadiumCollection,
        ...additionalStadiums.map(expect.objectContaining),
      );
      expect(comp.stadiumsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const game: IGame = { id: 5760 };
      const stadium: IStadium = { id: 27995 };
      game.stadium = stadium;

      activatedRoute.data = of({ game });
      comp.ngOnInit();

      expect(comp.stadiumsSharedCollection).toContainEqual(stadium);
      expect(comp.game).toEqual(game);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGame>>();
      const game = { id: 7137 };
      jest.spyOn(gameFormService, 'getGame').mockReturnValue(game);
      jest.spyOn(gameService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ game });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: game }));
      saveSubject.complete();

      // THEN
      expect(gameFormService.getGame).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(gameService.update).toHaveBeenCalledWith(expect.objectContaining(game));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGame>>();
      const game = { id: 7137 };
      jest.spyOn(gameFormService, 'getGame').mockReturnValue({ id: null });
      jest.spyOn(gameService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ game: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: game }));
      saveSubject.complete();

      // THEN
      expect(gameFormService.getGame).toHaveBeenCalled();
      expect(gameService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGame>>();
      const game = { id: 7137 };
      jest.spyOn(gameService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ game });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(gameService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareStadium', () => {
      it('Should forward to stadiumService', () => {
        const entity = { id: 27995 };
        const entity2 = { id: 9590 };
        jest.spyOn(stadiumService, 'compareStadium');
        comp.compareStadium(entity, entity2);
        expect(stadiumService.compareStadium).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
