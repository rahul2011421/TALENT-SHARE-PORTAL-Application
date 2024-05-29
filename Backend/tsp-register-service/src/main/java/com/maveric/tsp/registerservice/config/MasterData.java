package com.maveric.tsp.registerservice.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "tsp.masterdata")
@Data
@Slf4j
public class MasterData implements Serializable {
    private Map<String, List<String>> businessunits;
    private Map<String, List<String>> usergroups;
}
