package com.mg.backend;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

  private Map<String, List<String>> securityMapping = new HashMap<>();

  public Map<String, List<String>> getSecurityMapping() {
    return securityMapping;
  }

  public void setSecurityMapping(Map<String, List<String>> securityMapping) {
    this.securityMapping = securityMapping;
  }

}
