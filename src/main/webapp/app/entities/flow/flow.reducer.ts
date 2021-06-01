import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IFlow, defaultValue, IFlowDocumentState } from 'app/shared/model/flow.model';

export const ACTION_TYPES = {
  FETCH_FLOW_LIST: 'flow/FETCH_FLOW_LIST',
  FETCH_FLOW_LIST_BY_USER_ID: 'flow/FETCH_FLOW_LIST_BY_USER_ID',
  FETCH_FLOW: 'flow/FETCH_FLOW',
  CREATE_FLOW: 'flow/CREATE_FLOW',
  UPDATE_FLOW: 'flow/UPDATE_FLOW',
  PARTIAL_UPDATE_FLOW: 'flow/PARTIAL_UPDATE_FLOW',
  UPLOAD_FLOW_DOCUMENT_STATE: 'flow/UPLOAD_FLOW_DOCUMENT_STATE',
  DELETE_FLOW: 'flow/DELETE_FLOW',
  RESET: 'flow/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFlow>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type FlowState = Readonly<typeof initialState>;

// Reducer

export default (state: FlowState = initialState, action): FlowState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_FLOW_LIST_BY_USER_ID):
    case REQUEST(ACTION_TYPES.FETCH_FLOW_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FLOW):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_FLOW):
    case REQUEST(ACTION_TYPES.UPDATE_FLOW):
    case REQUEST(ACTION_TYPES.UPLOAD_FLOW_DOCUMENT_STATE):
    case REQUEST(ACTION_TYPES.DELETE_FLOW):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_FLOW):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_FLOW_LIST_BY_USER_ID):
    case FAILURE(ACTION_TYPES.FETCH_FLOW_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FLOW):
    case FAILURE(ACTION_TYPES.CREATE_FLOW):
    case FAILURE(ACTION_TYPES.UPDATE_FLOW):
    case FAILURE(ACTION_TYPES.UPLOAD_FLOW_DOCUMENT_STATE):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_FLOW):
    case FAILURE(ACTION_TYPES.DELETE_FLOW):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_FLOW_LIST):
    case SUCCESS(ACTION_TYPES.FETCH_FLOW_LIST_BY_USER_ID):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_FLOW):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_FLOW):
    case SUCCESS(ACTION_TYPES.UPDATE_FLOW):
    case SUCCESS(ACTION_TYPES.UPLOAD_FLOW_DOCUMENT_STATE):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_FLOW):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_FLOW):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/flows';

// Actions

export const getEntitiesByUserId: ICrudGetAllAction<IFlow> = userId => {
  const requestUrl = `${apiUrl}/by-user-id/${userId}`;
  return {
    type: ACTION_TYPES.FETCH_FLOW_LIST_BY_USER_ID,
    payload: axios.get<IFlow>(`${requestUrl}?cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntities: ICrudGetAllAction<IFlow> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : '?userId.equals=1'}`;
  return {
    type: ACTION_TYPES.FETCH_FLOW_LIST,
    payload: axios.get<IFlow>(`${requestUrl}&cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IFlow> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FLOW,
    payload: axios.get<IFlow>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IFlow> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FLOW,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IFlow> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FLOW,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IFlow> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_FLOW,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const uploadDocumentState: ICrudPutAction<IFlowDocumentState> = flowDocumentState => async dispatch => {
  console.log('inside uploadDocumentState');
  const result = await dispatch({
    type: ACTION_TYPES.UPLOAD_FLOW_DOCUMENT_STATE,
    payload: axios.put(`${apiUrl}/upload-document-state/${flowDocumentState.id}`, flowDocumentState.docState),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFlow> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FLOW,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
