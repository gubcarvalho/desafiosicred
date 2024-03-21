package com.nt.desafiosicred.services;

import com.nt.desafiosicred.enums.AgendaSessionStatus;
import com.nt.desafiosicred.exceptions.ResourceNotFoundException;
import com.nt.desafiosicred.exceptions.ValidationException;
import com.nt.desafiosicred.repositories.AgendaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SessionService {

    private final AgendaRepository agendaRepository;

    private final AgendaVowService agendaVowService;

    private final CloseSessionSchedulingService closeSessionSchedulingService;

    public void openSesion(
            final UUID agendaId,
            final Integer duration
    ) {
        final var agenda = agendaRepository.findById(agendaId)
                .orElseThrow(() -> new ResourceNotFoundException("agenda not found"));

        if (!AgendaSessionStatus.CLOSED.equals(agenda.sessionStatus())) {
            throw new ValidationException("Agenda voting already opened");
        }

        agenda.sessionStatus(AgendaSessionStatus.OPEN_FOR_VOTING);
        agendaRepository.save(agenda);

        closeSessionSchedulingService.scheduleATask(
                agendaId,
                () -> {
                    this.closeSession(agendaId);
                    closeSessionSchedulingService.removeScheduledTask(agendaId);
                },
                "0 0/%s * * * ?".formatted(duration)
        );
    }

    public void closeSession(final UUID agendaId) {
        agendaRepository.findById(agendaId)
                .filter(agenda -> !AgendaSessionStatus.CLOSED.equals(agenda.sessionStatus()))
                .ifPresent(agenda -> {
                    agenda.sessionStatus(AgendaSessionStatus.CLOSED);
                    log.info(
                            "session closed: {}",
                            agendaRepository.save(agenda)
                    );

                    agendaVowService.shareResults(agendaId);
                });
    }
}
