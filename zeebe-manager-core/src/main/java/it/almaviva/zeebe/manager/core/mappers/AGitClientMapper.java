package it.almaviva.zeebe.manager.core.mappers;

import org.mapstruct.Mapper;

import it.almaviva.zeebe.manager.domain.LogItemDTO;
import it.almaviva.zeebe.manager.integration.jgitclient.domain.LogResult;

@Mapper(componentModel = "spring")
public abstract class AGitClientMapper {

    public abstract LogItemDTO dtoToDomain(LogResult result);
}