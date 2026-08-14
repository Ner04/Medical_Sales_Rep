package com.mrsystem.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.mrsystem.domain.Role;
import com.mrsystem.domain.RoleRepository;
import com.mrsystem.domain.User;
import com.mrsystem.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
  @Autowired UserRepository users;
  @Autowired RoleRepository roles;
  @Autowired PasswordEncoder encoder;

  @Test
  void passwordEncoderMatchesSeedStylePassword() {
    Role role = new Role();
    role.setCode("USER");
    role.setName("Medical Representative");
    roles.save(role);

    User user = new User();
    user.setName("Test MR");
    user.setEmail("test@example.com");
    user.setUsername("testmr");
    user.setPasswordHash(encoder.encode("Password123!"));
    user.getRoles().add(role);
    users.save(user);

    assertThat(users.findByUsernameIgnoreCase("testmr")).isPresent();
    assertThat(encoder.matches("Password123!", user.getPasswordHash())).isTrue();
  }
}
