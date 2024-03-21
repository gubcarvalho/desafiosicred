package com.nt.desafiosicred.services;

import com.nt.desafiosicred.config.feign.HttpLoggerConfig;
import com.nt.desafiosicred.config.feign.TraceIdConfig;
import com.nt.desafiosicred.dtos.UserRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(
        name = "usersClient",
        url = "${app.integration.users-client.base-url}",
        configuration = {
                TraceIdConfig.class,
                HttpLoggerConfig.class
        })
public interface UserClient {

    @GetMapping(path = "${app.integration.users-client.check-user-endpoint}", consumes = APPLICATION_JSON_VALUE)
    UserRecord checkUser(@PathVariable("cpf") String cpf);
}
