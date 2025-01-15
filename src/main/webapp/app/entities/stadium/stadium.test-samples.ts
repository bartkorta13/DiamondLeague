import { IStadium, NewStadium } from './stadium.model';

export const sampleWithRequiredData: IStadium = {
  id: 20866,
  name: 'meanwhile these',
};

export const sampleWithPartialData: IStadium = {
  id: 28906,
  name: 'than',
  imagePath: 'gadzooks',
};

export const sampleWithFullData: IStadium = {
  id: 23575,
  name: 'intrepid after',
  imagePath: 'depot',
};

export const sampleWithNewData: NewStadium = {
  name: 'fence blah zowie',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
