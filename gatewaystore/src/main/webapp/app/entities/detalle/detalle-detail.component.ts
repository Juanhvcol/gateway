import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDetalle } from 'app/shared/model/detalle.model';

@Component({
  selector: 'jhi-detalle-detail',
  templateUrl: './detalle-detail.component.html'
})
export class DetalleDetailComponent implements OnInit {
  detalle: IDetalle | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detalle }) => (this.detalle = detalle));
  }

  previousState(): void {
    window.history.back();
  }
}
