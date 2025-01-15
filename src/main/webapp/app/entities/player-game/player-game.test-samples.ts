import { IPlayerGame, NewPlayerGame } from './player-game.model';

export const sampleWithRequiredData: IPlayerGame = {
  id: 25740,
  goals: 80,
  assists: 20,
};

export const sampleWithPartialData: IPlayerGame = {
  id: 13398,
  goals: 7,
  assists: 5,
  attackScore: 16555.61,
};

export const sampleWithFullData: IPlayerGame = {
  id: 31235,
  goals: 45,
  assists: 58,
  attackScore: 16915.96,
  defenseScore: 9869.43,
};

export const sampleWithNewData: NewPlayerGame = {
  goals: 84,
  assists: 8,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
