import dayjs from 'dayjs/esm';

import { IGame, NewGame } from './game.model';

export const sampleWithRequiredData: IGame = {
  id: 8692,
  date: dayjs('2025-01-15T01:56'),
};

export const sampleWithPartialData: IGame = {
  id: 19528,
  date: dayjs('2025-01-15T09:52'),
};

export const sampleWithFullData: IGame = {
  id: 6329,
  date: dayjs('2025-01-14T22:08'),
};

export const sampleWithNewData: NewGame = {
  date: dayjs('2025-01-15T09:57'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
