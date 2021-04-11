import './home.scss';

import React, {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';
import {Translate} from 'react-jhipster';
import {connect} from 'react-redux';
import {Row, Col, Alert} from 'reactstrap';
import {useEditorState, BangleEditor} from '@bangle.dev/react';
import {Plugin, SpecRegistry} from '@bangle.dev/core';
import {superscript, subscript} from '@bangle.dev/text-formatting';
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
import {
  floatingMenu,
  FloatingMenu,
  Menu,
  MenuGroup,
  BoldButton,
  HeadingButton,
} from '@bangle.dev/react-menu';
import {PluginKey} from '@bangle.dev/core';
import {Prism as SyntaxHighlighter} from 'react-syntax-highlighter';
import {solarizedlight} from 'react-syntax-highlighter/dist/esm/styles/prism';

import '@bangle.dev/core/style.css';
import '@bangle.dev/tooltip/style.css';
import '@bangle.dev/react-menu/style.css';

export type IHomeProp = StateProps;

const menuKey = new PluginKey('menuKey');

export const Home = (props: IHomeProp) => {
  const {account} = props;
  const [docState, setDocState] = useState(null)

  const updatePlugin = new Plugin({
    view: () => ({

      update(view, prevState) {
        if (!view.state.doc.eq(prevState.doc)) {
          //TODO send state to database
          setDocState(view.state.doc.toJSON());
        }
      },
    })
  })
  //TODO resolve account before setting
  const accountLogin = account.login;
  const editorState = useEditorState({
    specs: [
      bold.spec(),
      link.spec(),
      underline.spec(),
      doc.spec(),
      text.spec(),
      {...paragraph.spec(),
        schema: {
          ...paragraph.spec().schema,
          attrs: { userId: {  default: "accountLogin" } }
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
    initialValue: 'Hello world!',
  });
  //console.log("editorState.pmState.doc", editorState.pmState.doc.toJSON())
  return (
    <Row>
      <Col md="3" className="pad">{account.login}</Col>
      <Col md="9">
        <BangleEditor state={editorState}>
          <FloatingMenu menuKey={menuKey}
                        renderMenuType={({type}) => {
                          if (type === 'defaultMenu') {
                            return (
                              <Menu>
                                {/*<MenuGroup>*/}
                                {/*  <BoldButton/>*/}
                                {/*</MenuGroup>*/}
                                <MenuGroup>
                                  <HeadingButton level={1}/>
                                  <HeadingButton level={2}/>
                                </MenuGroup>
                              </Menu>
                            );
                          }

                          return null;
                        }}
          />
          <SyntaxHighlighter language="json" style={solarizedlight} showLineNumbers={true}>
            {JSON.stringify(docState, null, 1)}
          </SyntaxHighlighter>
        </BangleEditor>
      </Col>
    </Row>
  );
};


const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated,
});

type StateProps = ReturnType<typeof mapStateToProps>;

export default connect(mapStateToProps)(Home);
