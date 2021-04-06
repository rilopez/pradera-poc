import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './flow.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFlowDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FlowDetail = (props: IFlowDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { flowEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="flowDetailsHeading">
          <Translate contentKey="praderaApp.flow.detail.title">Flow</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{flowEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="praderaApp.flow.name">Name</Translate>
            </span>
          </dt>
          <dd>{flowEntity.name}</dd>
          <dt>
            <Translate contentKey="praderaApp.flow.user">User</Translate>
          </dt>
          <dd>{flowEntity.user ? flowEntity.user.login : ''}</dd>
          <dt>
            <Translate contentKey="praderaApp.flow.book">Book</Translate>
          </dt>
          <dd>{flowEntity.book ? flowEntity.book.title : ''}</dd>
          <dt>
            <Translate contentKey="praderaApp.flow.blocks">Blocks</Translate>
          </dt>
          <dd>
            {flowEntity.blocks
              ? flowEntity.blocks.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.content}</a>
                    {flowEntity.blocks && i === flowEntity.blocks.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/flow" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/flow/${flowEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ flow }: IRootState) => ({
  flowEntity: flow.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FlowDetail);
