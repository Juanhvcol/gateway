import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaystoreSharedModule } from 'app/shared/shared.module';
import { DetalleComponent } from './detalle.component';
import { DetalleDetailComponent } from './detalle-detail.component';
import { DetalleUpdateComponent } from './detalle-update.component';
import { DetalleDeleteDialogComponent } from './detalle-delete-dialog.component';
import { detalleRoute } from './detalle.route';

@NgModule({
  imports: [GatewaystoreSharedModule, RouterModule.forChild(detalleRoute)],
  declarations: [DetalleComponent, DetalleDetailComponent, DetalleUpdateComponent, DetalleDeleteDialogComponent],
  entryComponents: [DetalleDeleteDialogComponent]
})
export class GatewaystoreDetalleModule {}
