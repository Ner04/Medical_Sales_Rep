package com.mrsystem.domain;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, UUID> {
  long countByVisitDate(LocalDate visitDate);
  long countByVisitDateAndStatus(LocalDate visitDate, String status);
  long countByMrIdAndVisitDate(UUID mrId, LocalDate visitDate);
}
