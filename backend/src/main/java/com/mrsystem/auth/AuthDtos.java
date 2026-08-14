package com.mrsystem.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

public final class AuthDtos {
  private AuthDtos() {}

  public record LoginRequest(String companyCode, @NotBlank String username, @NotBlank String password) {}
  public record RefreshRequest(@NotBlank String refreshToken) {}
  public record ForgotPasswordRequest(@Email @NotBlank String email) {}
  public record ResetPasswordRequest(@NotBlank String token, @NotBlank String newPassword) {}
  public record ChangePasswordRequest(@NotBlank String currentPassword, @NotBlank String newPassword) {}
  public record SessionUser(UUID id, String name, String username, String email, List<String> roles, List<String> permissions) {}
  public record AuthResponse(String accessToken, String refreshToken, SessionUser user) {}
}
