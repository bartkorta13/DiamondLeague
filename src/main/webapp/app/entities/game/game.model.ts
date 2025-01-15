import dayjs from 'dayjs/esm';
import { IStadium } from 'app/entities/stadium/stadium.model';

export interface IGame {
  id: number;
  date?: dayjs.Dayjs | null;
  stadium?: Pick<IStadium, 'id'> | null;
}

export type NewGame = Omit<IGame, 'id'> & { id: null };
