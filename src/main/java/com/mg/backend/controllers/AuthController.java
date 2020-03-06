package com.mg.backend.controllers;

import com.mg.backend.models.Role;
import com.mg.backend.models.User;
import com.mg.backend.payload.request.LoginRequest;
import com.mg.backend.payload.request.SignupRequest;
import com.mg.backend.payload.response.JwtResponse;
import com.mg.backend.payload.response.MessageResponse;
import com.mg.backend.repository.UserRepository;
import com.mg.backend.security.jwt.JwtUtils;
import com.mg.backend.security.services.UserDetailsImpl;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final PasswordEncoder encoder;
  private final JwtUtils jwtUtils;

  @Autowired
  public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.encoder = encoder;
    this.jwtUtils = jwtUtils;
  }

  @PostMapping("/signin")
  public Single<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    var authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    var jwt = jwtUtils.generateJwtToken(authentication);

    var userDetails = (UserDetailsImpl) authentication.getPrincipal();
    var roles = userDetails.getAuthorities().stream()
      .map(GrantedAuthority::getAuthority)
      .collect(Collectors.toList());

    return Single.just(new JwtResponse(jwt,
      userDetails.getId(),
      userDetails.getUsername(),
      userDetails.getEmail(),
      roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
        .badRequest()
        .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
        .badRequest()
        .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    var user = new User(signUpRequest.getUsername(),
      signUpRequest.getEmail(),
      encoder.encode(signUpRequest.getPassword()));

    var strRoles = signUpRequest.getRoles();
    var roles = new HashSet<Role>();

    if (strRoles == null) {
      roles.add(Role.ROLE_USER);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
          case "admin":
            roles.add(Role.ROLE_ADMIN);
            break;
          case "mod":
            roles.add(Role.ROLE_MODERATOR);
            break;
          default:
            roles.add(Role.ROLE_USER);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
}
