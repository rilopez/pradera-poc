import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FlowBlock from './flow-block';
import FlowBlockDetail from './flow-block-detail';
import FlowBlockUpdate from './flow-block-update';
import FlowBlockDeleteDialog from './flow-block-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FlowBlockUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FlowBlockUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FlowBlockDetail} />
      <ErrorBoundaryRoute path={match.url} component={FlowBlock} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FlowBlockDeleteDialog} />
  </>
);

export default Routes;
