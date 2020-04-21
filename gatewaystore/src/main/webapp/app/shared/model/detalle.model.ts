export interface IDetalle {
  id?: number;
  nombre?: string;
}

export class Detalle implements IDetalle {
  constructor(public id?: number, public nombre?: string) {}
}
