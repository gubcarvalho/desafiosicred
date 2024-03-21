package com.nt.desafiosicred.queue;

import com.nt.desafiosicred.dtos.AgendaResultRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageSender {

    private final RabbitTemplate template;

    public void shareAgendaResults(final String queue, final AgendaResultRecord message) {
        log.info(message.toString());
        template.convertAndSend(queue, message);
    }
}
