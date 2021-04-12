import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('FlowBlock e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/flow-blocks*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('flow-block');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load FlowBlocks', () => {
    cy.intercept('GET', '/api/flow-blocks*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('flow-block');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('FlowBlock').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details FlowBlock page', () => {
    cy.intercept('GET', '/api/flow-blocks*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('flow-block');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('flowBlock');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create FlowBlock page', () => {
    cy.intercept('GET', '/api/flow-blocks*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('flow-block');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('FlowBlock');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit FlowBlock page', () => {
    cy.intercept('GET', '/api/flow-blocks*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('flow-block');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('FlowBlock');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of FlowBlock', () => {
    cy.intercept('GET', '/api/flow-blocks*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('flow-block');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('FlowBlock');

    cy.get(`[data-cy="blockOrder"]`).type('1708').should('have.value', '1708');

    cy.setFieldSelectToLastOfEntity('flow');

    cy.setFieldSelectToLastOfEntity('block');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/flow-blocks*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('flow-block');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of FlowBlock', () => {
    cy.intercept('GET', '/api/flow-blocks*').as('entitiesRequest');
    cy.intercept('GET', '/api/flow-blocks/*').as('dialogDeleteRequest');
    cy.intercept('DELETE', '/api/flow-blocks/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('flow-block');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('flowBlock').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/flow-blocks*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('flow-block');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
