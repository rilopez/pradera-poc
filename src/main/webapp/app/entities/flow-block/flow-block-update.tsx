import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IFlow } from 'app/shared/model/flow.model';
import { getEntities as getFlows } from 'app/entities/flow/flow.reducer';
import { IBlock } from 'app/shared/model/block.model';
import { getEntities as getBlocks } from 'app/entities/block/block.reducer';
import { getEntity, updateEntity, createEntity, reset } from './flow-block.reducer';
import { IFlowBlock } from 'app/shared/model/flow-block.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IFlowBlockUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FlowBlockUpdate = (props: IFlowBlockUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { flowBlockEntity, flows, blocks, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/flow-block' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getFlows();
    props.getBlocks();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...flowBlockEntity,
        ...values,
        flow: flows.find(it => it.id.toString() === values.flowId.toString()),
        block: blocks.find(it => it.id.toString() === values.blockId.toString()),
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="praderaApp.flowBlock.home.createOrEditLabel" data-cy="FlowBlockCreateUpdateHeading">
            <Translate contentKey="praderaApp.flowBlock.home.createOrEditLabel">Create or edit a FlowBlock</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : flowBlockEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="flow-block-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="flow-block-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="blockOrderLabel" for="flow-block-blockOrder">
                  <Translate contentKey="praderaApp.flowBlock.blockOrder">Block Order</Translate>
                </Label>
                <AvField
                  id="flow-block-blockOrder"
                  data-cy="blockOrder"
                  type="string"
                  className="form-control"
                  name="blockOrder"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="flow-block-flow">
                  <Translate contentKey="praderaApp.flowBlock.flow">Flow</Translate>
                </Label>
                <AvInput id="flow-block-flow" data-cy="flow" type="select" className="form-control" name="flowId">
                  <option value="" key="0" />
                  {flows
                    ? flows.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="flow-block-block">
                  <Translate contentKey="praderaApp.flowBlock.block">Block</Translate>
                </Label>
                <AvInput id="flow-block-block" data-cy="block" type="select" className="form-control" name="blockId">
                  <option value="" key="0" />
                  {blocks
                    ? blocks.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/flow-block" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  flows: storeState.flow.entities,
  blocks: storeState.block.entities,
  flowBlockEntity: storeState.flowBlock.entity,
  loading: storeState.flowBlock.loading,
  updating: storeState.flowBlock.updating,
  updateSuccess: storeState.flowBlock.updateSuccess,
});

const mapDispatchToProps = {
  getFlows,
  getBlocks,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FlowBlockUpdate);
