package com.mg.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

@Controller
public class OAuth2LoginController {

  @GetMapping("/me")
  public ResponseEntity currentUser(@AuthenticationPrincipal UserDetails userDetails) {
    Map<Object, Object> model = new HashMap<>();
    model.put("username", userDetails.getUsername());
    model.put("roles", userDetails.getAuthorities()
      .stream()
      .map(a -> ((GrantedAuthority) a).getAuthority())
      .collect(toList())
    );
    return ok(model);
  }
}
