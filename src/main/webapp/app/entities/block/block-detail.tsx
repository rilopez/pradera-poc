import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './block.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IBlockDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BlockDetail = (props: IBlockDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { blockEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="blockDetailsHeading">
          <Translate contentKey="praderaApp.block.detail.title">Block</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{blockEntity.id}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="praderaApp.block.type">Type</Translate>
            </span>
          </dt>
          <dd>{blockEntity.type}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="praderaApp.block.content">Content</Translate>
            </span>
          </dt>
          <dd>{blockEntity.content}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="praderaApp.block.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>{blockEntity.createdDate ? <TextFormat value={blockEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="hash">
              <Translate contentKey="praderaApp.block.hash">Hash</Translate>
            </span>
          </dt>
          <dd>{blockEntity.hash}</dd>
          <dt>
            <Translate contentKey="praderaApp.block.parent">Parent</Translate>
          </dt>
          <dd>{blockEntity.parent ? blockEntity.parent.content : ''}</dd>
          <dt>
            <Translate contentKey="praderaApp.block.user">User</Translate>
          </dt>
          <dd>{blockEntity.user ? blockEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/block" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/block/${blockEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ block }: IRootState) => ({
  blockEntity: block.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BlockDetail);
