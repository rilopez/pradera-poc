import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <MenuItem icon="asterisk" to="/block">
      <Translate contentKey="global.menu.entities.block" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/flow">
      <Translate contentKey="global.menu.entities.flow" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/book">
      <Translate contentKey="global.menu.entities.book" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/flow-block">
      <Translate contentKey="global.menu.entities.flowBlock" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
