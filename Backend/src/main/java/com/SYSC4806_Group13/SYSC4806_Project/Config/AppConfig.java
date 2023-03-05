package com.SYSC4806_Group13.SYSC4806_Project.Config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.ArrayList;
import java.util.List;


@Data
@EnableAsync
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    /**
     * Authorized redirect URLS from the frontend
     */
    private List<String> authorizedRedirectUris = new ArrayList<>();
    /**
     * The Secret for JWT token generation
     */
    private String tokenSecret;

    /**
     * Expiry of the Token in Msec default set in application.properties-> 864000000msec -> 10 days
     */
    private long tokenExpirationMsec;
}