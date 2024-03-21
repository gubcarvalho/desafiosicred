package com.nt.desafiosicred.dtos.mapping;

import com.nt.desafiosicred.dtos.AgendaRecord;
import com.nt.desafiosicred.enums.AgendaSessionStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AgendaMapperTest {

    private AgendaMapper mapper = AgendaMapper.INSTANCE;

    @Test
    void testHappyPath() {

        final var description = "any name";

        final var agenda = mapper.createAgenda(new AgendaRecord(null, description));
        assertNotNull(agenda);
        assertNull(agenda.id());
        assertEquals(description, agenda.description());
        assertEquals(AgendaSessionStatus.CLOSED, agenda.sessionStatus());

        final var agenda2 = mapper.createAgenda(new AgendaRecord(UUID.randomUUID(), "any name"));
        assertNotNull(agenda2);
        assertNull(agenda.id());
        assertEquals(description, agenda2.description());
        assertEquals(AgendaSessionStatus.CLOSED, agenda2.sessionStatus());
    }

}