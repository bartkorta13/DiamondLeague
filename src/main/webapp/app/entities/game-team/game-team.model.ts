import { IPlayer } from 'app/entities/player/player.model';
import { IGame } from 'app/entities/game/game.model';

export interface IGameTeam {
  id: number;
  goals?: number | null;
  captain?: Pick<IPlayer, 'id'> | null;
  game?: Pick<IGame, 'id'> | null;
}

export type NewGameTeam = Omit<IGameTeam, 'id'> & { id: null };
