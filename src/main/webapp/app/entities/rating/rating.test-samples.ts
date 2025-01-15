import dayjs from 'dayjs/esm';

import { IRating, NewRating } from './rating.model';

export const sampleWithRequiredData: IRating = {
  id: 9695,
  date: dayjs('2025-01-14T14:58'),
  attack: 92,
  defense: 72,
  engagement: 88,
  overall: 85,
};

export const sampleWithPartialData: IRating = {
  id: 16922,
  date: dayjs('2025-01-15T05:29'),
  attack: 56,
  defense: 59,
  engagement: 5,
  overall: 78,
};

export const sampleWithFullData: IRating = {
  id: 27357,
  date: dayjs('2025-01-15T05:30'),
  attack: 90,
  defense: 52,
  engagement: 73,
  overall: 96,
};

export const sampleWithNewData: NewRating = {
  date: dayjs('2025-01-15T07:18'),
  attack: 2,
  defense: 25,
  engagement: 40,
  overall: 84,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
