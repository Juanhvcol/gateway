import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'producto',
        loadChildren: () => import('./producto/producto.module').then(m => m.InventariomicroservicioProductoModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class InventariomicroservicioEntityModule {}
