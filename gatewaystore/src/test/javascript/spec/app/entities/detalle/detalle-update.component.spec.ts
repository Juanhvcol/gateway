import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewaystoreTestModule } from '../../../test.module';
import { DetalleUpdateComponent } from 'app/entities/detalle/detalle-update.component';
import { DetalleService } from 'app/entities/detalle/detalle.service';
import { Detalle } from 'app/shared/model/detalle.model';

describe('Component Tests', () => {
  describe('Detalle Management Update Component', () => {
    let comp: DetalleUpdateComponent;
    let fixture: ComponentFixture<DetalleUpdateComponent>;
    let service: DetalleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewaystoreTestModule],
        declarations: [DetalleUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DetalleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DetalleUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DetalleService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Detalle(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Detalle();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
