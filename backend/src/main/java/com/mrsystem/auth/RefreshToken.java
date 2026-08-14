package com.mrsystem.auth;

import com.mrsystem.common.AuditableEntity;
import com.mrsystem.domain.User;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken extends AuditableEntity {
  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
  private User user;
  @Column(name = "token_hash", nullable = false, unique = true)
  private String tokenHash;
  @Column(name = "expires_at", nullable = false)
  private Instant expiresAt;
  @Column(name = "revoked_at")
  private Instant revokedAt;

  public User getUser() { return user; }
  public void setUser(User user) { this.user = user; }
  public String getTokenHash() { return tokenHash; }
  public void setTokenHash(String tokenHash) { this.tokenHash = tokenHash; }
  public Instant getExpiresAt() { return expiresAt; }
  public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
  public Instant getRevokedAt() { return revokedAt; }
  public void setRevokedAt(Instant revokedAt) { this.revokedAt = revokedAt; }
  public boolean isActive() { return revokedAt == null && expiresAt.isAfter(Instant.now()); }
}
