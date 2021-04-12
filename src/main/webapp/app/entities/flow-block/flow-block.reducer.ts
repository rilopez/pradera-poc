import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IFlowBlock, defaultValue } from 'app/shared/model/flow-block.model';

export const ACTION_TYPES = {
  FETCH_FLOWBLOCK_LIST: 'flowBlock/FETCH_FLOWBLOCK_LIST',
  FETCH_FLOWBLOCK: 'flowBlock/FETCH_FLOWBLOCK',
  CREATE_FLOWBLOCK: 'flowBlock/CREATE_FLOWBLOCK',
  UPDATE_FLOWBLOCK: 'flowBlock/UPDATE_FLOWBLOCK',
  PARTIAL_UPDATE_FLOWBLOCK: 'flowBlock/PARTIAL_UPDATE_FLOWBLOCK',
  DELETE_FLOWBLOCK: 'flowBlock/DELETE_FLOWBLOCK',
  RESET: 'flowBlock/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFlowBlock>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type FlowBlockState = Readonly<typeof initialState>;

// Reducer

export default (state: FlowBlockState = initialState, action): FlowBlockState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_FLOWBLOCK_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FLOWBLOCK):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_FLOWBLOCK):
    case REQUEST(ACTION_TYPES.UPDATE_FLOWBLOCK):
    case REQUEST(ACTION_TYPES.DELETE_FLOWBLOCK):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_FLOWBLOCK):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_FLOWBLOCK_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FLOWBLOCK):
    case FAILURE(ACTION_TYPES.CREATE_FLOWBLOCK):
    case FAILURE(ACTION_TYPES.UPDATE_FLOWBLOCK):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_FLOWBLOCK):
    case FAILURE(ACTION_TYPES.DELETE_FLOWBLOCK):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_FLOWBLOCK_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_FLOWBLOCK):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_FLOWBLOCK):
    case SUCCESS(ACTION_TYPES.UPDATE_FLOWBLOCK):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_FLOWBLOCK):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_FLOWBLOCK):
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

const apiUrl = 'api/flow-blocks';

// Actions

export const getEntities: ICrudGetAllAction<IFlowBlock> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_FLOWBLOCK_LIST,
    payload: axios.get<IFlowBlock>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IFlowBlock> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FLOWBLOCK,
    payload: axios.get<IFlowBlock>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IFlowBlock> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FLOWBLOCK,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IFlowBlock> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FLOWBLOCK,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IFlowBlock> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_FLOWBLOCK,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFlowBlock> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FLOWBLOCK,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
