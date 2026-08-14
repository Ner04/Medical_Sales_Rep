package com.mrsystem.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasAuthority('MANAGE_USERS')")
public class UserAdminController {
  private final UserRepository users;
  private final RoleRepository roles;
  private final TerritoryRepository territories;
  private final PasswordEncoder encoder;

  public UserAdminController(UserRepository users, RoleRepository roles, TerritoryRepository territories, PasswordEncoder encoder) {
    this.users = users;
    this.roles = roles;
    this.territories = territories;
    this.encoder = encoder;
  }

  @GetMapping
  public Page<UserSummary> list(Pageable pageable) {
    return users.findAll(pageable).map(UserSummary::from);
  }

  @PostMapping
  public UserSummary create(@Valid @RequestBody UserRequest request) {
    User user = new User();
    apply(user, request);
    user.setPasswordHash(encoder.encode(request.password == null ? "Password123!" : request.password));
    return UserSummary.from(users.save(user));
  }

  @PutMapping("/{id}")
  public UserSummary update(@PathVariable UUID id, @Valid @RequestBody UserRequest request) {
    User user = users.findById(id).orElseThrow();
    apply(user, request);
    return UserSummary.from(users.save(user));
  }

  @PostMapping("/{id}/disable")
  public UserSummary disable(@PathVariable UUID id) {
    User user = users.findById(id).orElseThrow();
    user.setEnabled(false);
    return UserSummary.from(users.save(user));
  }

  @PostMapping("/{id}/activate")
  public UserSummary activate(@PathVariable UUID id) {
    User user = users.findById(id).orElseThrow();
    user.setEnabled(true);
    return UserSummary.from(users.save(user));
  }

  private void apply(User user, UserRequest request) {
    user.setCompanyCode(request.companyCode == null ? "MRX" : request.companyCode);
    user.setName(request.name);
    user.setEmail(request.email);
    user.setUsername(request.username);
    user.setMobile(request.mobile);
    user.setEmployeeId(request.employeeId);
    user.setDesignation(request.designation);
    user.setJoiningDate(request.joiningDate);
    if (request.territoryId != null) user.setTerritory(territories.findById(request.territoryId).orElseThrow());
    Set<Role> assigned = request.roleCodes == null || request.roleCodes.isEmpty()
        ? Set.of(roles.findByCode("USER").orElseThrow())
        : request.roleCodes.stream().map(code -> roles.findByCode(code).orElseThrow()).collect(java.util.stream.Collectors.toSet());
    user.setRoles(assigned);
  }

  public static class UserRequest {
    public String companyCode;
    @NotBlank public String name;
    @Email @NotBlank public String email;
    @NotBlank public String username;
    public String mobile;
    public String employeeId;
    public String designation;
    public String password;
    public UUID territoryId;
    public LocalDate joiningDate;
    public Set<String> roleCodes;
  }

  public record UserSummary(UUID id, String name, String email, String username, String designation, boolean enabled, Set<String> roles) {
    static UserSummary from(User user) {
      return new UserSummary(user.getId(), user.getName(), user.getEmail(), user.getUsername(), user.getDesignation(),
          user.isEnabled(), user.getRoles().stream().map(Role::getCode).collect(java.util.stream.Collectors.toSet()));
    }
  }
}
