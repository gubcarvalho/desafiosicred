package com.nt.desafiosicred.config.feign;

import feign.RequestInterceptor;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;

public class TraceIdConfig {

    public static final String MDC_ID = "mdcId";

    @Bean
    public RequestInterceptor traceIdInterceptor() {
        return request -> request.header(MDC_ID, MDC.get(MDC_ID));
    }

}
