import React, { useEffect, useState } from 'react';
import { BangleEditor, useEditorState } from '@bangle.dev/react';
import { Plugin, PluginKey } from '@bangle.dev/core';
import {
  blockquote,
  bold,
  bulletList,
  codeBlock,
  doc,
  hardBreak,
  heading,
  history,
  horizontalRule,
  image,
  link,
  listItem,
  orderedList,
  paragraph,
  text,
  underline,
} from '@bangle.dev/core/components';
import { floatingMenu, FloatingMenu, HeadingButton, Menu, MenuGroup, ParagraphButton } from '@bangle.dev/react-menu';
import { blockListToDocState } from 'app/shared/editor-utils';
import { IBlock } from 'app/shared/model/block.model';
const menuKey = new PluginKey('menuKey');

export interface IEditor {
  blockList: ReadonlyArray<IBlock>;
}

export const Editor = ({ blockList }: IEditor) => {
  if (!blockList || blockList.length === 0) {
    return null;
  }

  const initialState = blockListToDocState(blockList);

  const updatePlugin = new Plugin({
    view: () => ({
      update(view, prevState) {
        if (!view.state.doc.eq(prevState.doc)) {
          //TODO send state to database
          // eslint-disable-next-line no-console
          console.log(view.state.doc.toJSON());
        }
      },
    }),
  });

  const editorState = useEditorState({
    specs: [
      bold.spec(),
      link.spec(),
      underline.spec(),
      doc.spec(),
      text.spec(),
      {
        ...paragraph.spec(),
        schema: {
          ...paragraph.spec().schema,
          attrs: {
            userId: { default: '' },
            blockId: { default: '' },
          },
        },
      },
      blockquote.spec(),
      bulletList.spec(),
      codeBlock.spec(),
      hardBreak.spec(),
      heading.spec(),
      horizontalRule.spec(),
      listItem.spec(),
      orderedList.spec(),
      image.spec(),
    ],
    plugins: () => [
      bold.plugins(),
      paragraph.plugins(),
      hardBreak.plugins(),
      heading.plugins(),
      history.plugins(),
      updatePlugin,
      floatingMenu.plugins({
        key: menuKey,
      }),
    ],
    //TODO read initial value from database
    initialValue: initialState,
  });

  return (
    <BangleEditor state={editorState}>
      <FloatingMenu
        menuKey={menuKey}
        renderMenuType={({ type }) => {
          if (type === 'defaultMenu') {
            return (
              <Menu>
                {/*<MenuGroup>*/}
                {/*  <BoldButton/>*/}
                {/*</MenuGroup>*/}
                <MenuGroup>
                  <HeadingButton level={1} />
                  <ParagraphButton />
                </MenuGroup>
              </Menu>
            );
          }

          return null;
        }}
      />
    </BangleEditor>
  );
};
