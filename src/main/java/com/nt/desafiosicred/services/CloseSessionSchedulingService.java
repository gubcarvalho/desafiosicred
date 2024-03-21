package com.nt.desafiosicred.services;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class CloseSessionSchedulingService {

    private final TaskScheduler taskScheduler;

    private Map<UUID, ScheduledFuture<?>> jobs = new HashMap<>();

    public void scheduleATask(
            UUID agendaId,
            Runnable taskLet,
            String cronExpression
    ) {
        final var scheduledTask = taskScheduler.schedule(
                taskLet,
                new CronTrigger(
                        cronExpression,
                        TimeZone.getTimeZone(TimeZone.getDefault().getID())
                )
        );
        jobs.put(agendaId, scheduledTask);
    }

    public void removeScheduledTask(UUID agendaId) {
        final var scheduledTask = jobs.get(agendaId);
        if(scheduledTask != null) {
            scheduledTask.cancel(true);
            jobs.remove(agendaId);
        }
    }
}
