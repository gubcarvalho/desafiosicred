package com.nt.desafiosicred.services;

import com.nt.desafiosicred.dtos.AgendaRecord;
import com.nt.desafiosicred.dtos.mapping.AgendaMapper;
import com.nt.desafiosicred.enums.AgendaSessionStatus;
import com.nt.desafiosicred.exceptions.ValidationException;
import com.nt.desafiosicred.model.Agenda;
import com.nt.desafiosicred.repositories.AgendaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgendaServiceTest {

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private AgendaMapper agendaMapper;

    @InjectMocks
    private AgendaService agendaService;

    @Test
    void testCreateAgenda() {

        final var agendaRecord = new AgendaRecord(null, "any");

        final var expectedResult = new Agenda()
                .description(agendaRecord.description())
                .sessionStatus(AgendaSessionStatus.CLOSED);

        when(agendaMapper.createAgenda(agendaRecord)).thenReturn(expectedResult);
        when(agendaMapper.toRecord(any())).thenReturn(
                new AgendaRecord(UUID.randomUUID(), agendaRecord.description())
        );
        when(agendaRepository.save(any())).thenReturn(expectedResult);

        final var result = agendaService.createAgenda(agendaRecord);

        assertNotNull(result.id());
        assertEquals(result.description(), agendaRecord.description());
    }

    @Test
    void testException() {
        assertThrows(ValidationException.class,
                () -> agendaService.createAgenda(new AgendaRecord(null, "any"))
        );
    }
}