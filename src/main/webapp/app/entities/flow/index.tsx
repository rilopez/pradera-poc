import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Flow from './flow';
import FlowDetail from './flow-detail';
import FlowUpdate from './flow-update';
import FlowDeleteDialog from './flow-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FlowUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FlowUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FlowDetail} />
      <ErrorBoundaryRoute path={match.url} component={Flow} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FlowDeleteDialog} />
  </>
);

export default Routes;
