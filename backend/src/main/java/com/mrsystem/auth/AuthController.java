package com.mrsystem.auth;

import com.mrsystem.auth.AuthDtos.*;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public AuthResponse login(@Valid @RequestBody LoginRequest request) {
    return authService.login(request.companyCode(), request.username(), request.password());
  }

  @PostMapping("/refresh")
  public AuthResponse refresh(@Valid @RequestBody RefreshRequest request) {
    return authService.refresh(request.refreshToken());
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
    authService.forgotPassword(request.email());
    return ResponseEntity.ok(Map.of("message", "If the email exists, reset instructions have been sent"));
  }

  @PostMapping("/reset-password")
  public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
    authService.resetPassword(request.token(), request.newPassword());
    return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
  }

  @PostMapping("/change-password")
  public ResponseEntity<Map<String, String>> changePassword(Principal principal, @Valid @RequestBody ChangePasswordRequest request) {
    authService.changePassword(principal.getName(), request.currentPassword(), request.newPassword());
    return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
  }
}
