package com.ychy.cloud.gateway.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "uri")
@Data
public class UriConfigurationProperty {
    private String httpbin;
}
