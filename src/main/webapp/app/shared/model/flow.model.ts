import { IUser } from 'app/shared/model/user.model';
import { IBook } from 'app/shared/model/book.model';
import { IBlock } from 'app/shared/model/block.model';

export interface IFlow {
  id?: number;
  name?: string;
  user?: IUser | null;
  book?: IBook | null;
  blocks?: IBlock[] | null;
}

export const defaultValue: Readonly<IFlow> = {};
