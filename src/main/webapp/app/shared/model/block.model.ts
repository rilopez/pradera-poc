import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IFlowBlock } from 'app/shared/model/flow-block.model';
import { BlockType } from 'app/shared/model/enumerations/block-type.model';

export interface IBlock {
  id?: number;
  type?: BlockType;
  content?: string;
  createdDate?: string;
  hash?: string;
  parent?: IBlock | null;
  user?: IUser;
  parentBlocks?: IBlock[] | null;
  flows?: IFlowBlock[] | null;
}

export const defaultValue: Readonly<IBlock> = {};
