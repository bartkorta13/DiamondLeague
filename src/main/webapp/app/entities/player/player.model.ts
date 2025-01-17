import { IUser } from 'app/entities/user/user.model';
import { IClub } from 'app/entities/club/club.model';
import { Position } from 'app/entities/enumerations/position.model';

export interface IPlayer {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  nickname?: string | null;
  height?: number | null;
  yearOfBirth?: number | null;
  preferredPosition?: keyof typeof Position | null;
  appUser?: Pick<IUser, 'id' | 'login'> | null;
  favouriteClub?: Pick<IClub, 'id' | 'name'> | null;
}

export type NewPlayer = Omit<IPlayer, 'id'> & { id: null };
