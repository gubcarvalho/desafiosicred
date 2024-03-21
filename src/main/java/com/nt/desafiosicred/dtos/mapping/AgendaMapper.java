package com.nt.desafiosicred.dtos.mapping;

import com.nt.desafiosicred.dtos.AgendaRecord;
import com.nt.desafiosicred.model.Agenda;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AgendaMapper {

    public static final AgendaMapper INSTANCE = Mappers.getMapper(AgendaMapper.class);

    AgendaRecord toRecord(Agenda agenda);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sessionStatus", constant = "CLOSED")
    Agenda createAgenda(AgendaRecord agendaRecord);

}
