import './home.scss';

import React, { useEffect, useState } from 'react';
import { connect } from 'react-redux';
import { Col, Row } from 'reactstrap';

import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { solarizedlight } from 'react-syntax-highlighter/dist/esm/styles/prism';

import '@bangle.dev/core/style.css';
import '@bangle.dev/tooltip/style.css';
import '@bangle.dev/react-menu/style.css';
import { getEntitiesByUserId as getFlowsByUserId, uploadDocumentState } from 'app/entities/flow/flow.reducer';
import { getEntitiesByFlowId as getBlocksByFlowId } from 'app/entities/block/block.reducer';
import { blockListToDocState } from '../../shared/editor-utils';
import { Editor } from '../../shared/Editor';

export interface IHomeProp extends StateProps, DispatchProps {}

export const Home = (props: IHomeProp) => {
  const { account, flowList, blockList } = props;
  const [docState, setDocState] = useState(null);
  const [currentFlow, setCurrentFlow] = useState(null);

  const accountLogin = account.login;

  useEffect(() => {
    if (accountLogin) {
      props.getFlowsByUserId(account.id);
    }
  }, [accountLogin]);

  useEffect(() => {
    if (flowList.length) {
      setCurrentFlow(flowList[0]);
    }
  }, [flowList]);

  useEffect(() => {
    if (currentFlow) {
      props.getBlocksByFlowId(currentFlow.id);
    }
  }, [currentFlow]);

  useEffect(() => {
    if (blockList.length) {
      setDocState(blockListToDocState(blockList));
    }
  }, [blockList]);

  if (!accountLogin) return <div>no user logged</div>;

  function onChangeDocState(documentState: any): void {
    console.log('trying to send data to flow id', currentFlow.id);
    setDocState(documentState);
    props.uploadDocumentState({ id: currentFlow.id, docState: documentState });
  }

  return (
    <Row>
      <Col md="7" className="pad">
        <Editor blockList={blockList} userId={accountLogin.userId} onChange={onChangeDocState}></Editor>
      </Col>
      <Col md="5">
        <SyntaxHighlighter language="json" style={solarizedlight} showLineNumbers={true}>
          {JSON.stringify(docState, null, 1)}
        </SyntaxHighlighter>
      </Col>
    </Row>
  );
};

const mapStateToProps = storeState => {
  return {
    flowList: storeState.flow.entities,
    blockList: storeState.block.entities,
    account: storeState.authentication.account,
    isAuthenticated: storeState.authentication.isAuthenticated,
  };
};
const mapDispatchToProps = {
  getFlowsByUserId,
  getBlocksByFlowId,
  uploadDocumentState,
};
type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Home);
