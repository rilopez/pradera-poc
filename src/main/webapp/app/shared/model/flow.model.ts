import { IUser } from 'app/shared/model/user.model';
import { IBook } from 'app/shared/model/book.model';
import { IFlowBlock } from 'app/shared/model/flow-block.model';

export interface IFlow {
  id?: number;
  name?: string;
  user?: IUser | null;
  book?: IBook | null;
  blocks?: IFlowBlock[] | null;
}

export interface IFlowDocumentState {
  id?: number;
  docState?: any;
}

export const defaultValue: Readonly<IFlow> = {};
