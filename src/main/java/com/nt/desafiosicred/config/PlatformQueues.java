package com.nt.desafiosicred.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
@ConfigurationProperties("app.platform-queues")
public class PlatformQueues extends ArrayList<String> {

}
