import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../stadium.test-samples';

import { StadiumFormService } from './stadium-form.service';

describe('Stadium Form Service', () => {
  let service: StadiumFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StadiumFormService);
  });

  describe('Service methods', () => {
    describe('createStadiumFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStadiumFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            imagePath: expect.any(Object),
          }),
        );
      });

      it('passing IStadium should create a new form with FormGroup', () => {
        const formGroup = service.createStadiumFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            imagePath: expect.any(Object),
          }),
        );
      });
    });

    describe('getStadium', () => {
      it('should return NewStadium for default Stadium initial value', () => {
        const formGroup = service.createStadiumFormGroup(sampleWithNewData);

        const stadium = service.getStadium(formGroup) as any;

        expect(stadium).toMatchObject(sampleWithNewData);
      });

      it('should return NewStadium for empty Stadium initial value', () => {
        const formGroup = service.createStadiumFormGroup();

        const stadium = service.getStadium(formGroup) as any;

        expect(stadium).toMatchObject({});
      });

      it('should return IStadium', () => {
        const formGroup = service.createStadiumFormGroup(sampleWithRequiredData);

        const stadium = service.getStadium(formGroup) as any;

        expect(stadium).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStadium should not enable id FormControl', () => {
        const formGroup = service.createStadiumFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStadium should disable id FormControl', () => {
        const formGroup = service.createStadiumFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
