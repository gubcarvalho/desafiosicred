package com.nt.desafiosicred.services;

import com.nt.desafiosicred.config.PlatformQueues;
import com.nt.desafiosicred.dtos.AgendaResultRecord;
import com.nt.desafiosicred.enums.AgendaSessionStatus;
import com.nt.desafiosicred.enums.AgendaVowAnswer;
import com.nt.desafiosicred.exceptions.ResourceNotFoundException;
import com.nt.desafiosicred.exceptions.ValidationException;
import com.nt.desafiosicred.model.AgendaVow;
import com.nt.desafiosicred.queue.MessageSender;
import com.nt.desafiosicred.repositories.AgendaRepository;
import com.nt.desafiosicred.repositories.AgendaVowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class AgendaVowService {

    private final AgendaRepository agendaRepository;

    private final AgendaVowRepository agendaVowRepository;

    private final PlatformQueues platformQueues;

    private final MessageSender messageSender;

    private final UserClient userClient;

    @Transactional
    public void registerVow(
            final UUID agendaId,
            final String cpf,
            final boolean vow
    ) {
        final var agenda = agendaRepository.findById(agendaId)
                .orElseThrow(() -> new ResourceNotFoundException("agenda not found"));

        // check if the session is open
        if (!AgendaSessionStatus.OPEN_FOR_VOTING.equals(agenda.getSessionStatus())) {
            throw new ValidationException("Agenda voting not opened");
        }

        // check eligibility
        // this validation was commented because the users endpoint is not available anymore
//        if (!checkUser(cpf)) {
//            throw new ValidationException("User not able to vote");
//        }

        if (agendaVowRepository.existsByAgendaIdAndCpf(agenda.getId(), cpf)) {
            throw new ValidationException("Vote already registered");
        }

        // register vote
        log.info(
                "vote: {}",
                agendaVowRepository.save(new AgendaVow()
                        .agendaId(agenda.getId())
                        .cpf(cpf)
                        .vow(vow ? AgendaVowAnswer.YES : AgendaVowAnswer.NO)
                ));
    }

    private boolean checkUser(final String cpf) {
        try {
            return Optional.ofNullable(cpf)
                    .map(userClient::checkUser)
                    .filter(userRecord -> "ABLE_TO_VOTE".equals(userRecord.status()))
                    .isPresent();
        } catch(Exception e) {
            log.error("Could not retrieve user information: {}", e.toString());
            return false;
        }
    }

    public AgendaResultRecord getResults(final UUID agendaId) {

        final var agenda = agendaRepository.findById(agendaId)
                .orElseThrow(() -> new ResourceNotFoundException("agenda not found"));

        final var results = agendaVowRepository.findByAgendaId(agenda.getId())
                .stream()
                .collect(
                        Collectors.groupingBy(AgendaVow::vow, Collectors.counting())
                );

        final var formattedResult = new EnumMap<AgendaVowAnswer, Long>(AgendaVowAnswer.class);
        Arrays.stream(AgendaVowAnswer.values())
                .forEach(value -> formattedResult.put(value, Optional.ofNullable(results.get(value)).orElse(0L)));

        return new AgendaResultRecord(agendaId, agenda.getSessionStatus(), formattedResult);
    }

    /**
     * Share the agenda result with other systems in the platform
     *
     * NOTE: here i could have use a fanout message but i decide to implement
     * with queue configuration on the properties because there was no instructions about it
     * 
     * @param agendaId
     */
    public void shareResults(final UUID agendaId) {
        platformQueues.forEach(queueName -> messageSender.shareAgendaResults(
                queueName,
                getResults(agendaId))
        );
    }
}
