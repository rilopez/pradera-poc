import './home.scss';

import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { Row, Col, Alert } from 'reactstrap';
import { useEditorState, BangleEditor } from '@bangle.dev/react';
import { Plugin, SpecRegistry } from '@bangle.dev/core';
import { superscript, subscript } from '@bangle.dev/text-formatting';
import {
  history,
  bold,
  code,
  italic,
  strike,
  link,
  underline,
  doc,
  text,
  paragraph,
  blockquote,
  bulletList,
  codeBlock,
  hardBreak,
  heading,
  horizontalRule,
  listItem,
  orderedList,
  image,
} from '@bangle.dev/core/components';
import { floatingMenu, FloatingMenu, Menu, MenuGroup, ParagraphButton, HeadingButton } from '@bangle.dev/react-menu';
import { PluginKey } from '@bangle.dev/core';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { solarizedlight } from 'react-syntax-highlighter/dist/esm/styles/prism';

import '@bangle.dev/core/style.css';
import '@bangle.dev/tooltip/style.css';
import '@bangle.dev/react-menu/style.css';
import { getEntities as getFlows } from 'app/entities/flow/flow.reducer';

export interface IHomeProp extends StateProps, DispatchProps {}

const menuKey = new PluginKey('menuKey');

export const Home = (props: IHomeProp) => {
  const { account, flowList } = props;
  const [docState, setDocState] = useState(null);

  useEffect(() => {
    props.getFlows();
  }, []);

  const accountLogin = account.login;
  const updatePlugin = new Plugin({
    view: () => ({
      update(view, prevState) {
        if (!view.state.doc.eq(prevState.doc)) {
          //TODO send state to database
          setDocState(view.state.doc.toJSON());
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
            userId: { default: account.id },
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
      link.plugins(),
      underline.plugins(),
      paragraph.plugins(),
      blockquote.plugins(),
      bulletList.plugins(),
      codeBlock.plugins(),
      hardBreak.plugins(),
      heading.plugins(),
      horizontalRule.plugins(),
      listItem.plugins(),
      orderedList.plugins(),
      image.plugins(),
      history.plugins(),
      updatePlugin,
      floatingMenu.plugins({
        key: menuKey,
      }),
    ],
    //TODO create CSV initial value example src/main/resources/config/liquibase/fake-data/*.csv
    //TODO read initial value from database
    initialValue: {
      type: 'doc',
      content: [
        {
          type: 'heading',
          attrs: {
            level: 1,
            collapseContent: null,
          },
          content: [
            {
              type: 'text',
              text: 'Hello world! ',
            },
          ],
        },
        {
          type: 'paragraph',
          attrs: {
            nodeId: 'abe46c55-2286-459f-923a-d51380eb8362',
          },
          content: [
            {
              type: 'text',
              text: 'los grandes rentistas',
            },
          ],
        },
        {
          type: 'paragraph',
          attrs: {
            nodeId: 'edf414bd-b199-44fb-899e-98279cad0bd3',
          },
        },
      ],
    },
  });

  if (!accountLogin) return <div>no user logged</div>;

  return (
    <Row>
      <Col md="7" className="pad">
        <div>flowListLen: {flowList.length}</div>
        {/*TODO show flow menu */}
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
      </Col>
      <Col md="5">
        <SyntaxHighlighter language="json" style={solarizedlight} showLineNumbers={true}>
          {JSON.stringify(docState, null, 1)}
        </SyntaxHighlighter>
      </Col>
    </Row>
  );
};

const mapStateToProps = storeState => ({
  flowList: storeState.flow.entities,
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated,
});
const mapDispatchToProps = {
  getFlows, //TODO use getEntityByUserId
};
type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Home);
