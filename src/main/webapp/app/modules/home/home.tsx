import './home.scss';

import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { Row, Col, Alert } from 'reactstrap';
import { useEditorState, BangleEditor } from '@bangle.dev/react';
import { superscript, subscript } from '@bangle.dev/text-formatting';

import '@bangle.dev/core/style.css';

export type IHomeProp = StateProps;

export const Home = (props: IHomeProp) => {
  const { account } = props;

  return (
    <Row>
      <Col md="3" className="pad"></Col>
      <Col md="9">
        <Editor></Editor>
      </Col>
    </Row>
  );
};

function Editor() {
  const editorState = useEditorState({
    specs: [],
    initialValue: 'Hello world!',
  });
  return <BangleEditor state={editorState} />;
}

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated,
});

type StateProps = ReturnType<typeof mapStateToProps>;

export default connect(mapStateToProps)(Home);
