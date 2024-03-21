package com.nt.desafiosicred.dtos;

import com.nt.desafiosicred.enums.AgendaVowAnswer;

import java.util.Map;
import java.util.UUID;

public record AgendaResultRecord(
        UUID id,
        Map<AgendaVowAnswer, Long> results
) {}
