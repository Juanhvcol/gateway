import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDetalle } from 'app/shared/model/detalle.model';
import { DetalleService } from './detalle.service';

@Component({
  templateUrl: './detalle-delete-dialog.component.html'
})
export class DetalleDeleteDialogComponent {
  detalle?: IDetalle;

  constructor(protected detalleService: DetalleService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.detalleService.delete(id).subscribe(() => {
      this.eventManager.broadcast('detalleListModification');
      this.activeModal.close();
    });
  }
}
