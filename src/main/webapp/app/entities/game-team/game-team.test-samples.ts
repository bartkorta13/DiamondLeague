import { IGameTeam, NewGameTeam } from './game-team.model';

export const sampleWithRequiredData: IGameTeam = {
  id: 20804,
};

export const sampleWithPartialData: IGameTeam = {
  id: 2303,
  goals: 39,
};

export const sampleWithFullData: IGameTeam = {
  id: 9244,
  goals: 22,
};

export const sampleWithNewData: NewGameTeam = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
