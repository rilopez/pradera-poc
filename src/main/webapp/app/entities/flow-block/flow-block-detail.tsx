import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './flow-block.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFlowBlockDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FlowBlockDetail = (props: IFlowBlockDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { flowBlockEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="flowBlockDetailsHeading">
          <Translate contentKey="praderaApp.flowBlock.detail.title">FlowBlock</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{flowBlockEntity.id}</dd>
          <dt>
            <span id="blockOrder">
              <Translate contentKey="praderaApp.flowBlock.blockOrder">Block Order</Translate>
            </span>
          </dt>
          <dd>{flowBlockEntity.blockOrder}</dd>
          <dt>
            <Translate contentKey="praderaApp.flowBlock.flow">Flow</Translate>
          </dt>
          <dd>{flowBlockEntity.flow ? flowBlockEntity.flow.id : ''}</dd>
          <dt>
            <Translate contentKey="praderaApp.flowBlock.block">Block</Translate>
          </dt>
          <dd>{flowBlockEntity.block ? flowBlockEntity.block.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/flow-block" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/flow-block/${flowBlockEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ flowBlock }: IRootState) => ({
  flowBlockEntity: flowBlock.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FlowBlockDetail);
