import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IStadium } from '../stadium.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../stadium.test-samples';

import { StadiumService } from './stadium.service';

const requireRestSample: IStadium = {
  ...sampleWithRequiredData,
};

describe('Stadium Service', () => {
  let service: StadiumService;
  let httpMock: HttpTestingController;
  let expectedResult: IStadium | IStadium[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(StadiumService);
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

    it('should create a Stadium', () => {
      const stadium = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(stadium).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Stadium', () => {
      const stadium = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(stadium).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Stadium', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Stadium', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Stadium', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addStadiumToCollectionIfMissing', () => {
      it('should add a Stadium to an empty array', () => {
        const stadium: IStadium = sampleWithRequiredData;
        expectedResult = service.addStadiumToCollectionIfMissing([], stadium);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stadium);
      });

      it('should not add a Stadium to an array that contains it', () => {
        const stadium: IStadium = sampleWithRequiredData;
        const stadiumCollection: IStadium[] = [
          {
            ...stadium,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStadiumToCollectionIfMissing(stadiumCollection, stadium);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Stadium to an array that doesn't contain it", () => {
        const stadium: IStadium = sampleWithRequiredData;
        const stadiumCollection: IStadium[] = [sampleWithPartialData];
        expectedResult = service.addStadiumToCollectionIfMissing(stadiumCollection, stadium);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stadium);
      });

      it('should add only unique Stadium to an array', () => {
        const stadiumArray: IStadium[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const stadiumCollection: IStadium[] = [sampleWithRequiredData];
        expectedResult = service.addStadiumToCollectionIfMissing(stadiumCollection, ...stadiumArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const stadium: IStadium = sampleWithRequiredData;
        const stadium2: IStadium = sampleWithPartialData;
        expectedResult = service.addStadiumToCollectionIfMissing([], stadium, stadium2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stadium);
        expect(expectedResult).toContain(stadium2);
      });

      it('should accept null and undefined values', () => {
        const stadium: IStadium = sampleWithRequiredData;
        expectedResult = service.addStadiumToCollectionIfMissing([], null, stadium, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stadium);
      });

      it('should return initial array if no Stadium is added', () => {
        const stadiumCollection: IStadium[] = [sampleWithRequiredData];
        expectedResult = service.addStadiumToCollectionIfMissing(stadiumCollection, undefined, null);
        expect(expectedResult).toEqual(stadiumCollection);
      });
    });

    describe('compareStadium', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStadium(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 27995 };
        const entity2 = null;

        const compareResult1 = service.compareStadium(entity1, entity2);
        const compareResult2 = service.compareStadium(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 27995 };
        const entity2 = { id: 9590 };

        const compareResult1 = service.compareStadium(entity1, entity2);
        const compareResult2 = service.compareStadium(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 27995 };
        const entity2 = { id: 27995 };

        const compareResult1 = service.compareStadium(entity1, entity2);
        const compareResult2 = service.compareStadium(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
