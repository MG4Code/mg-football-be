package com.mg.backend.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class SignupRequest {
  @NotBlank
  @Size(min = 3, max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  private Set<String> roles;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;

  public String getUsername() {
    return username;
  }

  public SignupRequest setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public SignupRequest setEmail(String email) {
    this.email = email;
    return this;
  }

  public Set<String> getRoles() {
    return roles;
  }

  public SignupRequest setRoles(Set<String> roles) {
    this.roles = roles;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public SignupRequest setPassword(String password) {
    this.password = password;
    return this;
  }
}
