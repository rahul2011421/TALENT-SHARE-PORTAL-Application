package com.tsp.UserService.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@Data
public class MasterData implements Serializable {

    @Value("#{'${tsp.masterdata.skillcategories}'.split(',')}")
    private List<String> skillcategories;

    @Value("#{'${tsp.masterdata.technicalskills}'.split(',')}")
    private List<String> technicalskills;

    @Value("#{'${tsp.masterdata.domains}'.split(',')}")
    private List<String> domains;

    @PostConstruct
    public void postConstruction(){
        log.info("Master data skillcategories:"+skillcategories);
        log.info("Master data technicalskills:"+technicalskills);
        log.info("Master data domains:"+domains);
    }
}
