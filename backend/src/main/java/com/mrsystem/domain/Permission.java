package com.mrsystem.domain;

import com.mrsystem.common.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "permissions")
public class Permission extends AuditableEntity {
  @Column(nullable = false, unique = true)
  private String code;
  private String description;

  public String getCode() { return code; }
  public void setCode(String code) { this.code = code; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
}
