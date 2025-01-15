import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IGameTeam } from '../game-team.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../game-team.test-samples';

import { GameTeamService } from './game-team.service';

const requireRestSample: IGameTeam = {
  ...sampleWithRequiredData,
};

describe('GameTeam Service', () => {
  let service: GameTeamService;
  let httpMock: HttpTestingController;
  let expectedResult: IGameTeam | IGameTeam[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(GameTeamService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a GameTeam', () => {
      const gameTeam = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(gameTeam).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GameTeam', () => {
      const gameTeam = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(gameTeam).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GameTeam', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GameTeam', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a GameTeam', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addGameTeamToCollectionIfMissing', () => {
      it('should add a GameTeam to an empty array', () => {
        const gameTeam: IGameTeam = sampleWithRequiredData;
        expectedResult = service.addGameTeamToCollectionIfMissing([], gameTeam);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gameTeam);
      });

      it('should not add a GameTeam to an array that contains it', () => {
        const gameTeam: IGameTeam = sampleWithRequiredData;
        const gameTeamCollection: IGameTeam[] = [
          {
            ...gameTeam,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGameTeamToCollectionIfMissing(gameTeamCollection, gameTeam);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GameTeam to an array that doesn't contain it", () => {
        const gameTeam: IGameTeam = sampleWithRequiredData;
        const gameTeamCollection: IGameTeam[] = [sampleWithPartialData];
        expectedResult = service.addGameTeamToCollectionIfMissing(gameTeamCollection, gameTeam);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gameTeam);
      });

      it('should add only unique GameTeam to an array', () => {
        const gameTeamArray: IGameTeam[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const gameTeamCollection: IGameTeam[] = [sampleWithRequiredData];
        expectedResult = service.addGameTeamToCollectionIfMissing(gameTeamCollection, ...gameTeamArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const gameTeam: IGameTeam = sampleWithRequiredData;
        const gameTeam2: IGameTeam = sampleWithPartialData;
        expectedResult = service.addGameTeamToCollectionIfMissing([], gameTeam, gameTeam2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gameTeam);
        expect(expectedResult).toContain(gameTeam2);
      });

      it('should accept null and undefined values', () => {
        const gameTeam: IGameTeam = sampleWithRequiredData;
        expectedResult = service.addGameTeamToCollectionIfMissing([], null, gameTeam, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gameTeam);
      });

      it('should return initial array if no GameTeam is added', () => {
        const gameTeamCollection: IGameTeam[] = [sampleWithRequiredData];
        expectedResult = service.addGameTeamToCollectionIfMissing(gameTeamCollection, undefined, null);
        expect(expectedResult).toEqual(gameTeamCollection);
      });
    });

    describe('compareGameTeam', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGameTeam(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 31247 };
        const entity2 = null;

        const compareResult1 = service.compareGameTeam(entity1, entity2);
        const compareResult2 = service.compareGameTeam(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 31247 };
        const entity2 = { id: 28924 };

        const compareResult1 = service.compareGameTeam(entity1, entity2);
        const compareResult2 = service.compareGameTeam(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 31247 };
        const entity2 = { id: 31247 };

        const compareResult1 = service.compareGameTeam(entity1, entity2);
        const compareResult2 = service.compareGameTeam(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
