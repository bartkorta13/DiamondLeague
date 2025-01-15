import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IClub } from 'app/entities/club/club.model';
import { ClubService } from 'app/entities/club/service/club.service';
import { PlayerService } from '../service/player.service';
import { IPlayer } from '../player.model';
import { PlayerFormService } from './player-form.service';

import { PlayerUpdateComponent } from './player-update.component';

describe('Player Management Update Component', () => {
  let comp: PlayerUpdateComponent;
  let fixture: ComponentFixture<PlayerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let playerFormService: PlayerFormService;
  let playerService: PlayerService;
  let clubService: ClubService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PlayerUpdateComponent],
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
      .overrideTemplate(PlayerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlayerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    playerFormService = TestBed.inject(PlayerFormService);
    playerService = TestBed.inject(PlayerService);
    clubService = TestBed.inject(ClubService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Club query and add missing value', () => {
      const player: IPlayer = { id: 32153 };
      const favouriteClub: IClub = { id: 25440 };
      player.favouriteClub = favouriteClub;

      const clubCollection: IClub[] = [{ id: 25440 }];
      jest.spyOn(clubService, 'query').mockReturnValue(of(new HttpResponse({ body: clubCollection })));
      const additionalClubs = [favouriteClub];
      const expectedCollection: IClub[] = [...additionalClubs, ...clubCollection];
      jest.spyOn(clubService, 'addClubToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ player });
      comp.ngOnInit();

      expect(clubService.query).toHaveBeenCalled();
      expect(clubService.addClubToCollectionIfMissing).toHaveBeenCalledWith(
        clubCollection,
        ...additionalClubs.map(expect.objectContaining),
      );
      expect(comp.clubsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const player: IPlayer = { id: 32153 };
      const favouriteClub: IClub = { id: 25440 };
      player.favouriteClub = favouriteClub;

      activatedRoute.data = of({ player });
      comp.ngOnInit();

      expect(comp.clubsSharedCollection).toContainEqual(favouriteClub);
      expect(comp.player).toEqual(player);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlayer>>();
      const player = { id: 13390 };
      jest.spyOn(playerFormService, 'getPlayer').mockReturnValue(player);
      jest.spyOn(playerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ player });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: player }));
      saveSubject.complete();

      // THEN
      expect(playerFormService.getPlayer).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(playerService.update).toHaveBeenCalledWith(expect.objectContaining(player));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlayer>>();
      const player = { id: 13390 };
      jest.spyOn(playerFormService, 'getPlayer').mockReturnValue({ id: null });
      jest.spyOn(playerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ player: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: player }));
      saveSubject.complete();

      // THEN
      expect(playerFormService.getPlayer).toHaveBeenCalled();
      expect(playerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlayer>>();
      const player = { id: 13390 };
      jest.spyOn(playerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ player });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(playerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareClub', () => {
      it('Should forward to clubService', () => {
        const entity = { id: 25440 };
        const entity2 = { id: 10245 };
        jest.spyOn(clubService, 'compareClub');
        comp.compareClub(entity, entity2);
        expect(clubService.compareClub).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
