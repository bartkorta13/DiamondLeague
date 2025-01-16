import dayjs from 'dayjs/esm';
import { IPlayer } from 'app/entities/player/player.model';

export interface IRating {
  id: number;
  date?: dayjs.Dayjs | null;
  attack?: number | null;
  defense?: number | null;
  engagement?: number | null;
  overall?: number | null;
  player?: Pick<IPlayer, 'id' | 'nickname'> | null;
}

export type NewRating = Omit<IRating, 'id'> & { id: null };
