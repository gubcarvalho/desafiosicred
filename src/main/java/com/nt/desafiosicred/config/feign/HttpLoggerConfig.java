package com.nt.desafiosicred.config.feign;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public class HttpLoggerConfig {

    @Bean
    @Primary
    Logger feignLogger() {
        return new HttpLogger();
    }

}
