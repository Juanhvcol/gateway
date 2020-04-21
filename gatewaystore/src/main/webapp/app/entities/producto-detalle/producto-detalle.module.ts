import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaystoreSharedModule } from 'app/shared/shared.module';
import { ProductoDetalleComponent } from './producto-detalle.component';
import { ProductoDetalleDetailComponent } from './producto-detalle-detail.component';
import { ProductoDetalleUpdateComponent } from './producto-detalle-update.component';
import { ProductoDetalleDeleteDialogComponent } from './producto-detalle-delete-dialog.component';
import { productoDetalleRoute } from './producto-detalle.route';

@NgModule({
  imports: [GatewaystoreSharedModule, RouterModule.forChild(productoDetalleRoute)],
  declarations: [
    ProductoDetalleComponent,
    ProductoDetalleDetailComponent,
    ProductoDetalleUpdateComponent,
    ProductoDetalleDeleteDialogComponent
  ],
  entryComponents: [ProductoDetalleDeleteDialogComponent]
})
export class GatewaystoreProductoDetalleModule {}
