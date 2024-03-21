package com.nt.desafiosicred.config.feign;

import feign.Request;
import feign.Response;
import feign.Response.Body;
import feign.Util;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class HttpLogger extends Slf4jLogger {

    private static final String RESET  = "\u001B[0m";
    private static final String GREEN  = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN   = "\u001B[36m";

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        if (Objects.nonNull(request)) {
            log.info("[API Request] <{}{}:{}{}>, headers: '{}{}{}', body: '{}{}{}'",
            PURPLE, request.httpMethod(), request.url(), RESET,
            YELLOW, request.headers(), RESET,
            GREEN, getBody(request), RESET);
        }
    }

    private String getBody(Request request) {
        return (request.body() != null && request.charset() != null)
                ? new String(request.body(), request.charset())
                : StringUtils.EMPTY;
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        if (Objects.nonNull(response)) {
            String bodyStr = "";
            final int status = response.status();
            if (status != HttpStatus.NO_CONTENT.value() && status != HttpStatus.RESET_CONTENT.value()) {
                final Optional<Body> optBody = Optional.ofNullable(response.body());
                if (optBody.isPresent()) {
                    final Body body = optBody.get();
                    final byte[] bodyData = Util.toByteArray(body.asInputStream());
                    bodyStr = Util.decodeOrDefault(bodyData, Util.UTF_8, "")
                        .replaceAll("[\\r|\\n]", "")
                        .replaceAll("\\s{2,}", " ")
                        .replaceAll("\\s\"", "\"");
                    response = response.toBuilder().body(bodyData).build();
                }
            }
            log.info("[API Response] <{}{}:{}{}>, time: {}{} ms{}, headers: '{}{}{}', body: '{}{}{}'",
                PURPLE, status, HttpStatus.valueOf(status).getReasonPhrase(), RESET,
                CYAN, elapsedTime, RESET,
                YELLOW, response.headers(), RESET,
                GREEN, bodyStr, RESET);
        }
        return response;
    }

}
