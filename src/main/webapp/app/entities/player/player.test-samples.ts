import { IPlayer, NewPlayer } from './player.model';

export const sampleWithRequiredData: IPlayer = {
  id: 30462,
  firstName: 'Kari',
  lastName: 'Mills',
  nickname: 'worth frantically underneath',
  preferredPosition: 'MID',
};

export const sampleWithPartialData: IPlayer = {
  id: 23113,
  firstName: 'Joanny',
  lastName: 'Wilderman',
  nickname: 'instead exhausted',
  yearOfBirth: 1931,
  preferredPosition: 'MID',
};

export const sampleWithFullData: IPlayer = {
  id: 21150,
  firstName: 'Roberto',
  lastName: 'Ernser',
  nickname: 'supposing however calculus',
  height: 146,
  yearOfBirth: 2005,
  preferredPosition: 'DEF',
};

export const sampleWithNewData: NewPlayer = {
  firstName: 'Ona',
  lastName: 'Towne',
  nickname: 'roughly awful',
  preferredPosition: 'FW',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
