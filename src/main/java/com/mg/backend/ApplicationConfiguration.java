package com.mg.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ApplicationConfiguration extends WebSecurityConfigurerAdapter {

  private Logger logger = LoggerFactory.getLogger(ApplicationConfiguration.class);

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .authorizeRequests()
      .anyRequest().hasRole("READ")
      .and()
      .formLogin(withDefaults());
  }

  @Bean
  public UserDetailsService users() {
    UserDetails user = User.builder()
      .username("aigner.georg.79@gmail.com")
      .password("{bcrypt}$2y$12$tCtowHkQDXgwPXsnd0C2kOg/Z3mnpCXEVoOjC54MPbQktHRTsAvNm") // test1234
      .roles("READ", "WRITE")
      .build();
    UserDetails admin = User.builder()
      .username("mg4code@gmail.com")
      .password("{bcrypt}$2y$12$tCtowHkQDXgwPXsnd0C2kOg/Z3mnpCXEVoOjC54MPbQktHRTsAvNm") // test1234
      .roles("NIX")
      .build();
    return new InMemoryUserDetailsManager(user, admin);
  }

}
