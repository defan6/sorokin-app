package com.ddos.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "services.urls")
@Getter
@Setter
public class GatewayConfigurationProperties {


    private String eventManagerServiceUrl;
    private String eventNotificatorServiceUrl;
    private String authServiceUrl;
    private String profileServiceUrl;
    private String fileStorageServiceUrl;
}
