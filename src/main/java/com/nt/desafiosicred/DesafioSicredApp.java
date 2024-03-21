package com.nt.desafiosicred;

import com.nt.desafiosicred.services.UserClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
@EnableFeignClients(clients = {
        UserClient.class
})
public class DesafioSicredApp {

    public static void main(String[] args) {
        SpringApplication.run(DesafioSicredApp.class, args);
    }

}
