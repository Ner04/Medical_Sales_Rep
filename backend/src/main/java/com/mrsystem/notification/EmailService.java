package com.mrsystem.notification;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
  private final JavaMailSender mailSender;

  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Async
  public void sendPasswordReset(String to, String name, String resetUrl) {
    try {
      var message = mailSender.createMimeMessage();
      var helper = new MimeMessageHelper(message, true);
      helper.setTo(to);
      helper.setSubject("Reset your MR Field Force password");
      helper.setText(passwordResetTemplate(name, resetUrl), true);
      mailSender.send(message);
    } catch (Exception ignored) {
      // Keep password-reset enumeration safe; production logging can route this to monitoring.
    }
  }

  private String passwordResetTemplate(String name, String resetUrl) {
    return """
        <div style="font-family:Arial,sans-serif;background:#f4f7fb;padding:24px">
          <div style="max-width:560px;margin:auto;background:#fff;border-radius:8px;padding:28px;border:1px solid #e5e7eb">
            <h2 style="margin:0;color:#12395f">Password reset request</h2>
            <p>Hello %s,</p>
            <p>Use the secure link below to reset your password. It expires in 60 minutes.</p>
            <p><a style="background:#2563eb;color:#fff;padding:12px 18px;border-radius:6px;text-decoration:none" href="%s">Reset password</a></p>
            <p style="color:#64748b;font-size:13px">If you did not request this, you can ignore this email.</p>
          </div>
        </div>
        """.formatted(name, resetUrl);
  }
}
