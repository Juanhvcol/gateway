import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IProductoDetalle, ProductoDetalle } from 'app/shared/model/producto-detalle.model';
import { ProductoDetalleService } from './producto-detalle.service';

@Component({
  selector: 'jhi-producto-detalle-update',
  templateUrl: './producto-detalle-update.component.html'
})
export class ProductoDetalleUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    producto: []
  });

  constructor(
    protected productoDetalleService: ProductoDetalleService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productoDetalle }) => {
      this.updateForm(productoDetalle);
    });
  }

  updateForm(productoDetalle: IProductoDetalle): void {
    this.editForm.patchValue({
      id: productoDetalle.id,
      producto: productoDetalle.producto
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productoDetalle = this.createFromForm();
    if (productoDetalle.id !== undefined) {
      this.subscribeToSaveResponse(this.productoDetalleService.update(productoDetalle));
    } else {
      this.subscribeToSaveResponse(this.productoDetalleService.create(productoDetalle));
    }
  }

  private createFromForm(): IProductoDetalle {
    return {
      ...new ProductoDetalle(),
      id: this.editForm.get(['id'])!.value,
      producto: this.editForm.get(['producto'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductoDetalle>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
