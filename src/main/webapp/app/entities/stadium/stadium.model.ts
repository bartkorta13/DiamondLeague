export interface IStadium {
  id: number;
  name?: string | null;
  imagePath?: string | null;
}

export type NewStadium = Omit<IStadium, 'id'> & { id: null };
