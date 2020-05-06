import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'notificacion',
        loadChildren: () => import('./notificacion/notificacion.module').then(m => m.NotificacionesmicroservicioNotificacionModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class NotificacionesmicroservicioEntityModule {}
