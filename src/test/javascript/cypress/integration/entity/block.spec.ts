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

describe('Block e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/blocks*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('block');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Blocks', () => {
    cy.intercept('GET', '/api/blocks*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('block');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Block').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Block page', () => {
    cy.intercept('GET', '/api/blocks*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('block');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('block');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Block page', () => {
    cy.intercept('GET', '/api/blocks*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('block');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Block');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Block page', () => {
    cy.intercept('GET', '/api/blocks*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('block');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Block');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of Block', () => {
    cy.intercept('GET', '/api/blocks*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('block');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('Block');

    cy.get(`[data-cy="type"]`).select('PARAGRAPH');


    cy.get(`[data-cy="content"]`).type('override Dynamic', { force: true }).invoke('val').should('match', new RegExp('override Dynamic'));


    cy.get(`[data-cy="createdDate"]`).type('2021-03-30T04:52').invoke('val').should('equal', '2021-03-30T04:52');


    cy.get(`[data-cy="hash"]`).type('Licensed payment web-readiness', { force: true }).invoke('val').should('match', new RegExp('Licensed payment web-readiness'));

    cy.setFieldSelectToLastOfEntity('parent');

    cy.setFieldSelectToLastOfEntity('user');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/blocks*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('block');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of Block', () => {
    cy.intercept('GET', '/api/blocks*').as('entitiesRequest');
    cy.intercept('GET', '/api/blocks/*').as('dialogDeleteRequest');
    cy.intercept('DELETE', '/api/blocks/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('block');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('block').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/blocks*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('block');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
