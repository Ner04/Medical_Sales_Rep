package com.mrsystem.domain;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {
  @EntityGraph(attributePaths = {"roles", "roles.permissions"})
  Optional<User> findByUsernameIgnoreCase(String username);
  @EntityGraph(attributePaths = {"roles", "roles.permissions"})
  Optional<User> findByEmailIgnoreCase(String email);
  long countByEnabledTrue();
}
