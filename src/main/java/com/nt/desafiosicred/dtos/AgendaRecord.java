package com.nt.desafiosicred.dtos;

import java.util.UUID;

public record AgendaRecord(
        UUID id,
        String description
) {}
