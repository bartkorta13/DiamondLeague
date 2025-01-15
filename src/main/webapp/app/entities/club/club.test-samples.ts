import { IClub, NewClub } from './club.model';

export const sampleWithRequiredData: IClub = {
  id: 6270,
  name: 'blah hence',
};

export const sampleWithPartialData: IClub = {
  id: 22157,
  name: 'hovel flood diagram',
  logoPath: 'schlep mainstream',
};

export const sampleWithFullData: IClub = {
  id: 32609,
  name: 'materialise',
  logoPath: 'offend',
};

export const sampleWithNewData: NewClub = {
  name: 'and dime',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
