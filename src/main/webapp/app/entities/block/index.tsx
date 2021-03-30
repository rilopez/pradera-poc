import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Block from './block';
import BlockDetail from './block-detail';
import BlockUpdate from './block-update';
import BlockDeleteDialog from './block-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BlockUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BlockUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BlockDetail} />
      <ErrorBoundaryRoute path={match.url} component={Block} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BlockDeleteDialog} />
  </>
);

export default Routes;
