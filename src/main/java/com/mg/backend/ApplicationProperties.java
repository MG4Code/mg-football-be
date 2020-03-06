package com.mg.backend;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "football")
public class ApplicationProperties {

  private String jwtSecret;
  private long jwtExpirationMs;

  public String getJwtSecret() {
    return jwtSecret;
  }

  public ApplicationProperties setJwtSecret(String jwtSecret) {
    this.jwtSecret = jwtSecret;
    return this;
  }

  public long getJwtExpirationMs() {
    return jwtExpirationMs;
  }

  public ApplicationProperties setJwtExpirationMs(long jwtExpirationMs) {
    this.jwtExpirationMs = jwtExpirationMs;
    return this;
  }
}
