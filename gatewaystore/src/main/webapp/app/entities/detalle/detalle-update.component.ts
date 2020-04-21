import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IDetalle, Detalle } from 'app/shared/model/detalle.model';
import { DetalleService } from './detalle.service';

@Component({
  selector: 'jhi-detalle-update',
  templateUrl: './detalle-update.component.html'
})
export class DetalleUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: []
  });

  constructor(protected detalleService: DetalleService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detalle }) => {
      this.updateForm(detalle);
    });
  }

  updateForm(detalle: IDetalle): void {
    this.editForm.patchValue({
      id: detalle.id,
      nombre: detalle.nombre
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const detalle = this.createFromForm();
    if (detalle.id !== undefined) {
      this.subscribeToSaveResponse(this.detalleService.update(detalle));
    } else {
      this.subscribeToSaveResponse(this.detalleService.create(detalle));
    }
  }

  private createFromForm(): IDetalle {
    return {
      ...new Detalle(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDetalle>>): void {
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
