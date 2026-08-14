package com.mrsystem.security;

import com.mrsystem.domain.Permission;
import com.mrsystem.domain.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
  private final UserRepository users;

  public DatabaseUserDetailsService(UserRepository users) {
    this.users = users;
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    var user = users.findByUsernameIgnoreCase(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    var authorities = user.getRoles().stream()
        .flatMap(role -> role.getPermissions().stream())
        .map(Permission::getCode)
        .distinct()
        .map(SimpleGrantedAuthority::new)
        .toList();
    return org.springframework.security.core.userdetails.User
        .withUsername(user.getUsername())
        .password(user.getPasswordHash())
        .authorities(authorities)
        .accountLocked(user.isAccountLocked())
        .disabled(!user.isEnabled())
        .build();
  }
}
