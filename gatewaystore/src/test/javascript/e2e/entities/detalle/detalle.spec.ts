import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { DetalleComponentsPage, DetalleDeleteDialog, DetalleUpdatePage } from './detalle.page-object';

const expect = chai.expect;

describe('Detalle e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let detalleComponentsPage: DetalleComponentsPage;
  let detalleUpdatePage: DetalleUpdatePage;
  let detalleDeleteDialog: DetalleDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Detalles', async () => {
    await navBarPage.goToEntity('detalle');
    detalleComponentsPage = new DetalleComponentsPage();
    await browser.wait(ec.visibilityOf(detalleComponentsPage.title), 5000);
    expect(await detalleComponentsPage.getTitle()).to.eq('gatewaystoreApp.detalle.home.title');
    await browser.wait(ec.or(ec.visibilityOf(detalleComponentsPage.entities), ec.visibilityOf(detalleComponentsPage.noResult)), 1000);
  });

  it('should load create Detalle page', async () => {
    await detalleComponentsPage.clickOnCreateButton();
    detalleUpdatePage = new DetalleUpdatePage();
    expect(await detalleUpdatePage.getPageTitle()).to.eq('gatewaystoreApp.detalle.home.createOrEditLabel');
    await detalleUpdatePage.cancel();
  });

  it('should create and save Detalles', async () => {
    const nbButtonsBeforeCreate = await detalleComponentsPage.countDeleteButtons();

    await detalleComponentsPage.clickOnCreateButton();

    await promise.all([detalleUpdatePage.setNombreInput('nombre')]);

    expect(await detalleUpdatePage.getNombreInput()).to.eq('nombre', 'Expected Nombre value to be equals to nombre');

    await detalleUpdatePage.save();
    expect(await detalleUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await detalleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Detalle', async () => {
    const nbButtonsBeforeDelete = await detalleComponentsPage.countDeleteButtons();
    await detalleComponentsPage.clickOnLastDeleteButton();

    detalleDeleteDialog = new DetalleDeleteDialog();
    expect(await detalleDeleteDialog.getDialogTitle()).to.eq('gatewaystoreApp.detalle.delete.question');
    await detalleDeleteDialog.clickOnConfirmButton();

    expect(await detalleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
