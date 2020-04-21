import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ProductoDetalleComponentsPage, ProductoDetalleDeleteDialog, ProductoDetalleUpdatePage } from './producto-detalle.page-object';

const expect = chai.expect;

describe('ProductoDetalle e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let productoDetalleComponentsPage: ProductoDetalleComponentsPage;
  let productoDetalleUpdatePage: ProductoDetalleUpdatePage;
  let productoDetalleDeleteDialog: ProductoDetalleDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ProductoDetalles', async () => {
    await navBarPage.goToEntity('producto-detalle');
    productoDetalleComponentsPage = new ProductoDetalleComponentsPage();
    await browser.wait(ec.visibilityOf(productoDetalleComponentsPage.title), 5000);
    expect(await productoDetalleComponentsPage.getTitle()).to.eq('gatewaystoreApp.productoDetalle.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(productoDetalleComponentsPage.entities), ec.visibilityOf(productoDetalleComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ProductoDetalle page', async () => {
    await productoDetalleComponentsPage.clickOnCreateButton();
    productoDetalleUpdatePage = new ProductoDetalleUpdatePage();
    expect(await productoDetalleUpdatePage.getPageTitle()).to.eq('gatewaystoreApp.productoDetalle.home.createOrEditLabel');
    await productoDetalleUpdatePage.cancel();
  });

  it('should create and save ProductoDetalles', async () => {
    const nbButtonsBeforeCreate = await productoDetalleComponentsPage.countDeleteButtons();

    await productoDetalleComponentsPage.clickOnCreateButton();

    await promise.all([productoDetalleUpdatePage.setProductoInput('producto')]);

    expect(await productoDetalleUpdatePage.getProductoInput()).to.eq('producto', 'Expected Producto value to be equals to producto');

    await productoDetalleUpdatePage.save();
    expect(await productoDetalleUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await productoDetalleComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last ProductoDetalle', async () => {
    const nbButtonsBeforeDelete = await productoDetalleComponentsPage.countDeleteButtons();
    await productoDetalleComponentsPage.clickOnLastDeleteButton();

    productoDetalleDeleteDialog = new ProductoDetalleDeleteDialog();
    expect(await productoDetalleDeleteDialog.getDialogTitle()).to.eq('gatewaystoreApp.productoDetalle.delete.question');
    await productoDetalleDeleteDialog.clickOnConfirmButton();

    expect(await productoDetalleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
