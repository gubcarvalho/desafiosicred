package com.nt.desafiosicred.controllers;

import com.nt.desafiosicred.dtos.AgendaRecord;
import com.nt.desafiosicred.dtos.AgendaResultRecord;
import com.nt.desafiosicred.services.AgendaService;
import com.nt.desafiosicred.services.AgendaVowService;
import com.nt.desafiosicred.services.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(summary = "Create an agenda")
    @PostMapping
    public AgendaRecord createAgenda(
            @RequestBody
            final AgendaRecord agendaRecord
    ) {
        return agendaService.createAgenda(agendaRecord);
    }

    @Operation(summary = "Open a voting session for the requested agenda")
    @PutMapping("/{id}/open-session")
    public void doOpenSession(
            @Parameter(description = "id da agenda")
            @PathVariable("id") UUID id,
            @Parameter(description = "duration (in minutes) for the voting session")
            @RequestParam(value = "duration", required = false, defaultValue = "1") Integer duration
    ) {
        sessionService.openSesion(id, duration);
    }

    @Operation(summary = "Register the user vow")
    @PutMapping("/{uuid}/vote")
    public void doVoting(
            @Parameter(description = "id da agenda")
            @PathVariable("uuid") UUID uuid,
            @Parameter(description = "User's CPF")
            @RequestParam("cpf") @CPF String cpf,
            @Parameter(description = "User vow")
            @RequestParam("vow") boolean vow
    ) {
        agendaVowService.registerVow(uuid, cpf, vow);
    }

    @Operation(summary = "Get agenda results")
    @GetMapping("/{uuid}")
    public AgendaResultRecord getResult(
            @Parameter(description = "id da agenda")
            @PathVariable("uuid") UUID uuid
    ) {
        return agendaVowService.getResults(uuid);
    }
}
