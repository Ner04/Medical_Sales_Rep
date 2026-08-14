package com.mrsystem.auth;

import com.mrsystem.auth.AuthDtos.AuthResponse;
import com.mrsystem.auth.AuthDtos.SessionUser;
import com.mrsystem.domain.Permission;
import com.mrsystem.domain.Role;
import com.mrsystem.domain.User;
import com.mrsystem.domain.UserRepository;
import com.mrsystem.notification.EmailService;
import com.mrsystem.security.JwtService;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
  private final AuthenticationManager authenticationManager;
  private final UserRepository users;
  private final JwtService jwtService;
  private final RefreshTokenRepository refreshTokens;
  private final PasswordResetTokenRepository resetTokens;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;
  private final long refreshDays;
  private final String frontendUrl;
  private final SecureRandom random = new SecureRandom();

  public AuthService(AuthenticationManager authenticationManager, UserRepository users, JwtService jwtService,
                     RefreshTokenRepository refreshTokens, PasswordResetTokenRepository resetTokens,
                     PasswordEncoder passwordEncoder, EmailService emailService,
                     @Value("${app.jwt.refresh-token-days}") long refreshDays,
                     @Value("${app.frontend-url}") String frontendUrl) {
    this.authenticationManager = authenticationManager;
    this.users = users;
    this.jwtService = jwtService;
    this.refreshTokens = refreshTokens;
    this.resetTokens = resetTokens;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
    this.refreshDays = refreshDays;
    this.frontendUrl = frontendUrl;
  }

  @Transactional
  public AuthResponse login(String companyCode, String username, String password) {
    Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    SecurityContextHolder.getContext().setAuthentication(auth);
    User user = users.findByUsernameIgnoreCase(username).orElseThrow();
    if (companyCode != null && !companyCode.isBlank() && !user.getCompanyCode().equalsIgnoreCase(companyCode)) {
      throw new IllegalArgumentException("Company code does not match this user");
    }
    user.setLastLoginAt(Instant.now());
    String access = jwtService.createAccessToken(user);
    String refresh = newOpaqueToken();
    RefreshToken entity = new RefreshToken();
    entity.setUser(user);
    entity.setTokenHash(sha256(refresh));
    entity.setExpiresAt(Instant.now().plusSeconds(refreshDays * 86400));
    refreshTokens.save(entity);
    return new AuthResponse(access, refresh, toSessionUser(user));
  }

  @Transactional
  public AuthResponse refresh(String refreshToken) {
    RefreshToken token = refreshTokens.findByTokenHash(sha256(refreshToken))
        .filter(RefreshToken::isActive)
        .orElseThrow(() -> new IllegalArgumentException("Refresh token is invalid or expired"));
    User user = users.findByUsernameIgnoreCase(token.getUser().getUsername()).orElseThrow();
    return new AuthResponse(jwtService.createAccessToken(user), refreshToken, toSessionUser(user));
  }

  @Transactional
  public void forgotPassword(String email) {
    users.findByEmailIgnoreCase(email).ifPresent(user -> {
      String token = newOpaqueToken();
      PasswordResetToken reset = new PasswordResetToken();
      reset.setUser(user);
      reset.setTokenHash(sha256(token));
      reset.setExpiresAt(Instant.now().plusSeconds(3600));
      resetTokens.save(reset);
      emailService.sendPasswordReset(user.getEmail(), user.getName(), frontendUrl + "/reset-password?token=" + token);
    });
  }

  @Transactional
  public void resetPassword(String token, String newPassword) {
    PasswordResetToken reset = resetTokens.findByTokenHash(sha256(token))
        .filter(PasswordResetToken::isActive)
        .orElseThrow(() -> new IllegalArgumentException("Reset token is invalid or expired"));
    User user = reset.getUser();
    user.setPasswordHash(passwordEncoder.encode(newPassword));
    reset.setUsedAt(Instant.now());
  }

  @Transactional
  public void changePassword(String username, String currentPassword, String newPassword) {
    User user = users.findByUsernameIgnoreCase(username).orElseThrow();
    if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
      throw new IllegalArgumentException("Current password is incorrect");
    }
    user.setPasswordHash(passwordEncoder.encode(newPassword));
  }

  private SessionUser toSessionUser(User user) {
    List<String> roles = user.getRoles().stream().map(Role::getCode).sorted().toList();
    List<String> permissions = user.getRoles().stream()
        .flatMap(role -> role.getPermissions().stream())
        .map(Permission::getCode)
        .distinct()
        .sorted()
        .toList();
    return new SessionUser(user.getId(), user.getName(), user.getUsername(), user.getEmail(), roles, permissions);
  }

  private String newOpaqueToken() {
    byte[] bytes = new byte[48];
    random.nextBytes(bytes);
    return HexFormat.of().formatHex(bytes);
  }

  private String sha256(String input) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      return HexFormat.of().formatHex(digest.digest(input.getBytes(StandardCharsets.UTF_8)));
    } catch (Exception ex) {
      throw new IllegalStateException("Unable to hash token", ex);
    }
  }
}
