package com.nt.desafiosicred.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
@RequiredArgsConstructor
public class RabbitConfig {

    public static final String QUEUE_OPEN_SESSION = "desafio-sicred.agenda.open-session";

    public static final String QUEUE_AGENDA_RESULTS = "desafio-sicred.agenda.results";

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SmartInitializingSingleton reconfigureCf(final CachingConnectionFactory cf) {
        return () -> cf.setConnectionNameStrategy(f -> applicationName);
    }

    @Bean
    public Queue agendaOpenSession() {
        return QueueBuilder.durable(QUEUE_OPEN_SESSION)
                .build();
    }
}
