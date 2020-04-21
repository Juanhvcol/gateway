export interface IProductoDetalle {
  id?: number;
  producto?: string;
}

export class ProductoDetalle implements IProductoDetalle {
  constructor(public id?: number, public producto?: string) {}
}
