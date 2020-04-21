import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDetalle, Detalle } from 'app/shared/model/detalle.model';
import { DetalleService } from './detalle.service';
import { DetalleComponent } from './detalle.component';
import { DetalleDetailComponent } from './detalle-detail.component';
import { DetalleUpdateComponent } from './detalle-update.component';

@Injectable({ providedIn: 'root' })
export class DetalleResolve implements Resolve<IDetalle> {
  constructor(private service: DetalleService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDetalle> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((detalle: HttpResponse<Detalle>) => {
          if (detalle.body) {
            return of(detalle.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Detalle());
  }
}

export const detalleRoute: Routes = [
  {
    path: '',
    component: DetalleComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'gatewaystoreApp.detalle.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DetalleDetailComponent,
    resolve: {
      detalle: DetalleResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewaystoreApp.detalle.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DetalleUpdateComponent,
    resolve: {
      detalle: DetalleResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewaystoreApp.detalle.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DetalleUpdateComponent,
    resolve: {
      detalle: DetalleResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewaystoreApp.detalle.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
