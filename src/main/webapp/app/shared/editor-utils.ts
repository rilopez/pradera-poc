import { IBlock } from 'app/shared/model/block.model';
import { BlockType } from './model/enumerations/block-type.model';

function getContentType(blockType: BlockType): string {
  switch (blockType) {
    case BlockType.TITLE:
      return 'heading';
    case BlockType.PARAGRAPH:
    default:
      return 'paragraph';
  }
}

function blockToContentNode(block: IBlock) {
  let type;
  let attrs;
  const content = [
    {
      type: 'text',
      text: block.content,
    },
  ];
  const commonAttr = { blockId: block.id };

  switch (block.type) {
    case BlockType.TITLE:
      type = 'heading';
      attrs = { ...commonAttr, level: 1, collapseContent: null };
      break;
    case BlockType.PARAGRAPH:
    default:
      type = 'paragraph';
      attrs = { ...commonAttr };
  }

  return {
    type,
    attrs,
    content,
  };
}

export function blockListToDocState(blockList: ReadonlyArray<IBlock>) {
  const contentNodes = blockList.map(b => blockToContentNode(b));

  return {
    type: 'doc',
    content: contentNodes,
  };
}
