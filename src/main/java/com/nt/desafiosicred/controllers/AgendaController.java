package com.nt.desafiosicred.controllers;

import com.nt.desafiosicred.dtos.AgendaRecord;
import com.nt.desafiosicred.dtos.AgendaResultRecord;
import com.nt.desafiosicred.services.AgendaService;
import com.nt.desafiosicred.services.AgendaVowService;
import com.nt.desafiosicred.services.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequestMapping("/agenda")
@RequiredArgsConstructor
public class AgendaController {

    private final AgendaService agendaService;

    private final AgendaVowService agendaVowService;

    private final SessionService sessionService;

    @PostMapping
    public AgendaRecord createAgenda(
            @RequestBody
            final AgendaRecord agendaRecord
    ) {
        return agendaService.createAgenda(agendaRecord);
    }

    @PutMapping("/{id}/open-session")
    public void doOpenSession(
            @PathVariable("id") UUID id,
            @RequestParam(value = "duration", required = false, defaultValue = "1") Integer duration
    ) {
        sessionService.openSesion(id, duration);
    }

    @PutMapping("/{uuid}/vote")
    public void doVoting(
            @PathVariable("uuid") UUID uuid,
            @RequestParam("cpf") @CPF String cpf,
            @RequestParam("vow") boolean vow
    ) {
        agendaVowService.registerVow(uuid, cpf, vow);
    }

    @GetMapping("/{uuid}")
    public AgendaResultRecord getResult(
            @PathVariable("uuid") UUID uuid
    ) {
        return agendaVowService.getResults(uuid);
    }
}
