import { element, by, ElementFinder } from 'protractor';

export class ProductoDetalleComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-producto-detalle div table .btn-danger'));
  title = element.all(by.css('jhi-producto-detalle div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class ProductoDetalleUpdatePage {
  pageTitle = element(by.id('jhi-producto-detalle-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  productoInput = element(by.id('field_producto'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setProductoInput(producto: string): Promise<void> {
    await this.productoInput.sendKeys(producto);
  }

  async getProductoInput(): Promise<string> {
    return await this.productoInput.getAttribute('value');
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class ProductoDetalleDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-productoDetalle-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-productoDetalle'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
