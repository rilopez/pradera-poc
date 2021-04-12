import { IFlow } from 'app/shared/model/flow.model';
import { IBlock } from 'app/shared/model/block.model';

export interface IFlowBlock {
  id?: number;
  blockOrder?: number;
  flow?: IFlow | null;
  block?: IBlock | null;
}

export const defaultValue: Readonly<IFlowBlock> = {};
