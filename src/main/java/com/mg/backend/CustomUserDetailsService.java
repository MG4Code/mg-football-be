package com.mg.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService extends ClientDetailsUserDetailsService {

  @Autowired
  public CustomUserDetailsService() {
    super(new InMemoryClientDetailsService());
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    return super.loadUserByUsername(username);
  }
}
