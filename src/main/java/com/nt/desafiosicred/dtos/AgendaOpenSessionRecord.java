package com.nt.desafiosicred.dtos;

import java.util.UUID;

public record AgendaOpenSessionRecord(
        UUID id,
        Integer duration
) {}
