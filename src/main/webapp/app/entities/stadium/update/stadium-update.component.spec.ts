import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { StadiumService } from '../service/stadium.service';
import { IStadium } from '../stadium.model';
import { StadiumFormService } from './stadium-form.service';

import { StadiumUpdateComponent } from './stadium-update.component';

describe('Stadium Management Update Component', () => {
  let comp: StadiumUpdateComponent;
  let fixture: ComponentFixture<StadiumUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let stadiumFormService: StadiumFormService;
  let stadiumService: StadiumService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [StadiumUpdateComponent],
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
      .overrideTemplate(StadiumUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StadiumUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    stadiumFormService = TestBed.inject(StadiumFormService);
    stadiumService = TestBed.inject(StadiumService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const stadium: IStadium = { id: 9590 };

      activatedRoute.data = of({ stadium });
      comp.ngOnInit();

      expect(comp.stadium).toEqual(stadium);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStadium>>();
      const stadium = { id: 27995 };
      jest.spyOn(stadiumFormService, 'getStadium').mockReturnValue(stadium);
      jest.spyOn(stadiumService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stadium });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stadium }));
      saveSubject.complete();

      // THEN
      expect(stadiumFormService.getStadium).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(stadiumService.update).toHaveBeenCalledWith(expect.objectContaining(stadium));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStadium>>();
      const stadium = { id: 27995 };
      jest.spyOn(stadiumFormService, 'getStadium').mockReturnValue({ id: null });
      jest.spyOn(stadiumService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stadium: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stadium }));
      saveSubject.complete();

      // THEN
      expect(stadiumFormService.getStadium).toHaveBeenCalled();
      expect(stadiumService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStadium>>();
      const stadium = { id: 27995 };
      jest.spyOn(stadiumService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stadium });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(stadiumService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
