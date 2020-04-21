package com.themakers.storeonline.service.mapper;


import com.themakers.storeonline.domain.*;
import com.themakers.storeonline.service.dto.ClienteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cliente} and its DTO {@link ClienteDTO}.
 */
@Mapper(componentModel = "spring", uses = {FacturaMapper.class})
public interface ClienteMapper extends EntityMapper<ClienteDTO, Cliente> {

    @Mapping(source = "factura.id", target = "facturaId")
    ClienteDTO toDto(Cliente cliente);

    @Mapping(source = "facturaId", target = "factura")
    Cliente toEntity(ClienteDTO clienteDTO);

    default Cliente fromId(Long id) {
        if (id == null) {
            return null;
        }
        Cliente cliente = new Cliente();
        cliente.setId(id);
        return cliente;
    }
}
