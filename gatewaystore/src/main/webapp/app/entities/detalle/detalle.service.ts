import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDetalle } from 'app/shared/model/detalle.model';

type EntityResponseType = HttpResponse<IDetalle>;
type EntityArrayResponseType = HttpResponse<IDetalle[]>;

@Injectable({ providedIn: 'root' })
export class DetalleService {
  public resourceUrl = SERVER_API_URL + 'api/detalles';

  constructor(protected http: HttpClient) {}

  create(detalle: IDetalle): Observable<EntityResponseType> {
    return this.http.post<IDetalle>(this.resourceUrl, detalle, { observe: 'response' });
  }

  update(detalle: IDetalle): Observable<EntityResponseType> {
    return this.http.put<IDetalle>(this.resourceUrl, detalle, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDetalle>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDetalle[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
