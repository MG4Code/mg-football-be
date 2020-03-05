package com.mg.backend;

import com.mg.backend.jwt.JwtSecurityConfigurer;
import com.mg.backend.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.util.HashSet;
import java.util.List;

import static java.lang.String.format;

@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
public class ApplicationConfiguration extends WebSecurityConfigurerAdapter {

  private Logger logger = LoggerFactory.getLogger(ApplicationConfiguration.class);

  private final ApplicationProperties applicationProperties;
  private final JwtTokenProvider jwtTokenProvider;


  @Autowired
  public ApplicationConfiguration(ApplicationProperties applicationProperties, JwtTokenProvider jwtTokenProvider) {
    this.applicationProperties = applicationProperties;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .httpBasic().disable()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
        .authorizeRequests()
        .antMatchers("/auth/signin").permitAll()
        .antMatchers(HttpMethod.GET, "/player/**").hasRole("ROLE")
        .antMatchers(HttpMethod.DELETE, "/player/**").hasRole("ADMIN")
        .anyRequest().authenticated()
      .and()
      .apply(new JwtSecurityConfigurer(jwtTokenProvider))
      .and()
      .oauth2Login();
    ;
  }

  @Bean
  public GrantedAuthoritiesMapper userAuthoritiesMapper() {
    return (authorities) -> {
      var mappedAuthorities = new HashSet<GrantedAuthority>();
      authorities.forEach(authority -> {
        String uniqueId = null;
        if (authority instanceof OidcUserAuthority) {
          uniqueId = ((OidcUserAuthority) authority).getIdToken().getEmail();
        } else if (authority instanceof OAuth2UserAuthority) {
          uniqueId = ((OAuth2UserAuthority) authority).getAttributes().get("email").toString();
        }
        if (uniqueId != null) {
          applicationProperties.getSecurityMapping().getOrDefault(format("[%s]", uniqueId), List.of())
            .forEach(s -> {
              logger.info("assign role {}", s);
              mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + s));
            });
          if (mappedAuthorities.isEmpty()) {
            logger.warn("no roles found for {}", uniqueId);
          }
        } else {
          logger.error("unable to get unique id from a provider");
        }
      });
      return mappedAuthorities;
    };
  }

}
