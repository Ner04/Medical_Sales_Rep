package com.mrsystem.domain;

import com.mrsystem.common.AuditableEntity;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
public class AuditLog extends AuditableEntity {
  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id")
  private User user;
  @Column(nullable = false)
  private String action;
  @Column(name = "entity_type")
  private String entityType;
  @Column(name = "entity_id")
  private UUID entityId;
  @Column(name = "ip_address")
  private String ipAddress;
  private String details;

  public User getUser() { return user; }
  public void setUser(User user) { this.user = user; }
  public String getAction() { return action; }
  public void setAction(String action) { this.action = action; }
  public String getEntityType() { return entityType; }
  public void setEntityType(String entityType) { this.entityType = entityType; }
  public UUID getEntityId() { return entityId; }
  public void setEntityId(UUID entityId) { this.entityId = entityId; }
  public String getIpAddress() { return ipAddress; }
  public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
  public String getDetails() { return details; }
  public void setDetails(String details) { this.details = details; }
}
