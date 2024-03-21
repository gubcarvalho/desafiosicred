package com.nt.desafiosicred.services;

import com.nt.desafiosicred.dtos.AgendaRecord;
import com.nt.desafiosicred.dtos.mapping.AgendaMapper;
import com.nt.desafiosicred.exceptions.ValidationException;
import com.nt.desafiosicred.repositories.AgendaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgendaService {

    private final AgendaRepository agendaRepository;

    private final AgendaMapper agendaMapper;

    @Transactional
    public AgendaRecord createAgenda(final AgendaRecord agendaRecord) {
        return Optional.ofNullable(agendaRecord)
                .map(agendaMapper::createAgenda)
                .map(agendaRepository::save)
                .map(agendaMapper::toRecord)
                .orElseThrow(() -> new ValidationException("could not save the agenda"));
    }
}
