import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { setFileData, byteSize, Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntities as getBlocks } from 'app/entities/block/block.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './block.reducer';
import { IBlock } from 'app/shared/model/block.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IBlockUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BlockUpdate = (props: IBlockUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { blockEntity, blocks, users, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/block' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getBlocks();
    props.getUsers();
  }, []);

  const onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
    props.setBlob(name, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.createdDate = convertDateTimeToServer(values.createdDate);

    if (errors.length === 0) {
      const entity = {
        ...blockEntity,
        ...values,
        parent: blocks.find(it => it.id.toString() === values.parentId.toString()),
        user: users.find(it => it.id.toString() === values.userId.toString()),
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
          <h2 id="praderaApp.block.home.createOrEditLabel" data-cy="BlockCreateUpdateHeading">
            <Translate contentKey="praderaApp.block.home.createOrEditLabel">Create or edit a Block</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : blockEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="block-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="block-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="typeLabel" for="block-type">
                  <Translate contentKey="praderaApp.block.type">Type</Translate>
                </Label>
                <AvInput
                  id="block-type"
                  data-cy="type"
                  type="select"
                  className="form-control"
                  name="type"
                  value={(!isNew && blockEntity.type) || 'TITLE'}
                >
                  <option value="TITLE">{translate('praderaApp.BlockType.TITLE')}</option>
                  <option value="PARAGRAPH">{translate('praderaApp.BlockType.PARAGRAPH')}</option>
                  <option value="CHAPTER">{translate('praderaApp.BlockType.CHAPTER')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="contentLabel" for="block-content">
                  <Translate contentKey="praderaApp.block.content">Content</Translate>
                </Label>
                <AvInput
                  id="block-content"
                  data-cy="content"
                  type="textarea"
                  name="content"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="createdDateLabel" for="block-createdDate">
                  <Translate contentKey="praderaApp.block.createdDate">Created Date</Translate>
                </Label>
                <AvInput
                  id="block-createdDate"
                  data-cy="createdDate"
                  type="datetime-local"
                  className="form-control"
                  name="createdDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.blockEntity.createdDate)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="hashLabel" for="block-hash">
                  <Translate contentKey="praderaApp.block.hash">Hash</Translate>
                </Label>
                <AvField
                  id="block-hash"
                  data-cy="hash"
                  type="text"
                  name="hash"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="block-parent">
                  <Translate contentKey="praderaApp.block.parent">Parent</Translate>
                </Label>
                <AvInput id="block-parent" data-cy="parent" type="select" className="form-control" name="parentId">
                  <option value="" key="0" />
                  {blocks
                    ? blocks.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.content}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="block-user">
                  <Translate contentKey="praderaApp.block.user">User</Translate>
                </Label>
                <AvInput id="block-user" data-cy="user" type="select" className="form-control" name="userId" required>
                  <option value="" key="0" />
                  {users
                    ? users.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.login}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/block" replace color="info">
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
  blocks: storeState.block.entities,
  users: storeState.userManagement.users,
  blockEntity: storeState.block.entity,
  loading: storeState.block.loading,
  updating: storeState.block.updating,
  updateSuccess: storeState.block.updateSuccess,
});

const mapDispatchToProps = {
  getBlocks,
  getUsers,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BlockUpdate);
