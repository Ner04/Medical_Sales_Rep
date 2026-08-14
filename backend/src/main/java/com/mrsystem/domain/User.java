package com.mrsystem.domain;

import com.mrsystem.common.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends AuditableEntity {
  @Column(name = "company_code", nullable = false)
  private String companyCode = "MRX";
  @Column(nullable = false)
  private String name;
  @Column(nullable = false, unique = true)
  private String email;
  @Column(nullable = false, unique = true)
  private String username;
  private String mobile;
  @Column(name = "employee_id")
  private String employeeId;
  private String designation;
  @Column(name = "password_hash", nullable = false)
  private String passwordHash;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "territory_id")
  private Territory territory;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reporting_manager_id")
  private User reportingManager;
  @Column(name = "joining_date")
  private LocalDate joiningDate;
  private boolean enabled = true;
  @Column(name = "account_locked")
  private boolean accountLocked;
  @Column(name = "last_login_at")
  private Instant lastLoginAt;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  public String getCompanyCode() { return companyCode; }
  public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }
  public String getMobile() { return mobile; }
  public void setMobile(String mobile) { this.mobile = mobile; }
  public String getEmployeeId() { return employeeId; }
  public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
  public String getDesignation() { return designation; }
  public void setDesignation(String designation) { this.designation = designation; }
  public String getPasswordHash() { return passwordHash; }
  public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
  public Territory getTerritory() { return territory; }
  public void setTerritory(Territory territory) { this.territory = territory; }
  public User getReportingManager() { return reportingManager; }
  public void setReportingManager(User reportingManager) { this.reportingManager = reportingManager; }
  public LocalDate getJoiningDate() { return joiningDate; }
  public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }
  public boolean isEnabled() { return enabled; }
  public void setEnabled(boolean enabled) { this.enabled = enabled; }
  public boolean isAccountLocked() { return accountLocked; }
  public void setAccountLocked(boolean accountLocked) { this.accountLocked = accountLocked; }
  public Instant getLastLoginAt() { return lastLoginAt; }
  public void setLastLoginAt(Instant lastLoginAt) { this.lastLoginAt = lastLoginAt; }
  public Set<Role> getRoles() { return roles; }
  public void setRoles(Set<Role> roles) { this.roles = roles; }
}
