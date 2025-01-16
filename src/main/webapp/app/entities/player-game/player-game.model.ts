import { IPlayer } from 'app/entities/player/player.model';
import { IGameTeam } from 'app/entities/game-team/game-team.model';

export interface IPlayerGame {
  id: number;
  goals?: number | null;
  assists?: number | null;
  attackScore?: number | null;
  defenseScore?: number | null;
  player?: Pick<IPlayer, 'id' | 'nickname'> | null;
  gameTeam?: Pick<IGameTeam, 'id'> | null;
}

export type NewPlayerGame = Omit<IPlayerGame, 'id'> & { id: null };
