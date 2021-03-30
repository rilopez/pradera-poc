import { Moment } from 'moment';
import { BlockType } from 'app/shared/model/enumerations/block-type.model';

export interface IBlock {
  id?: number;
  type?: BlockType;
  content?: string;
  createdDate?: string;
  hash?: string;
  parentContent?: string;
  parentId?: number;
  userLogin?: string;
  userId?: number;
}

export const defaultValue: Readonly<IBlock> = {};
