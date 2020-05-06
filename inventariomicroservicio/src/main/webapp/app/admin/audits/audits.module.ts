import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { InventariomicroservicioSharedModule } from 'app/shared/shared.module';

import { AuditsComponent } from './audits.component';

import { auditsRoute } from './audits.route';

@NgModule({
  imports: [InventariomicroservicioSharedModule, RouterModule.forChild([auditsRoute])],
  declarations: [AuditsComponent]
})
export class AuditsModule {}
