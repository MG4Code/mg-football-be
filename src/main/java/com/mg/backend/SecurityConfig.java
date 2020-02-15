package com.mg.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
      .anyRequest().hasRole("BAR")
//      .authenticated()
      .and()
      .oauth2Login();
  }

  @Bean
  public GrantedAuthoritiesMapper userAuthoritiesMapper() {
    return (authorities) -> {
      Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
      authorities.forEach(authority -> {
        if (authority instanceof OidcUserAuthority) {
          OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;

          OidcIdToken idToken = oidcUserAuthority.getIdToken();
          OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();

          // Map the claims found in idToken and/or userInfo
          // to one or more GrantedAuthority's and add it to mappedAuthorities
          logger.debug(idToken.toString());

          // TODO assign roles bases on attributes
          mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_BAR"));
        }
      });
      return mappedAuthorities;
    };
  }


}
