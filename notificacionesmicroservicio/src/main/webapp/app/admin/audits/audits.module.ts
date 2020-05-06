import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NotificacionesmicroservicioSharedModule } from 'app/shared/shared.module';

import { AuditsComponent } from './audits.component';

import { auditsRoute } from './audits.route';

@NgModule({
  imports: [NotificacionesmicroservicioSharedModule, RouterModule.forChild([auditsRoute])],
  declarations: [AuditsComponent]
})
export class AuditsModule {}
