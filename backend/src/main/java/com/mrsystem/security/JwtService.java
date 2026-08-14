package com.mrsystem.security;

import com.mrsystem.domain.Permission;
import com.mrsystem.domain.Role;
import com.mrsystem.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  private final SecretKey key;
  private final long accessMinutes;

  public JwtService(@Value("${app.jwt.secret}") String secret,
                    @Value("${app.jwt.access-token-minutes}") long accessMinutes) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.accessMinutes = accessMinutes;
  }

  public String createAccessToken(User user) {
    Instant now = Instant.now();
    List<String> roles = user.getRoles().stream().map(Role::getCode).sorted().toList();
    List<String> permissions = user.getRoles().stream()
        .flatMap(role -> role.getPermissions().stream())
        .map(Permission::getCode)
        .distinct()
        .sorted()
        .toList();
    return Jwts.builder()
        .subject(user.getUsername())
        .claim("uid", user.getId().toString())
        .claim("roles", roles)
        .claim("permissions", permissions)
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusSeconds(accessMinutes * 60)))
        .signWith(key)
        .compact();
  }

  public Claims parse(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
  }
}
