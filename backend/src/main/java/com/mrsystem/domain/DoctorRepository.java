package com.mrsystem.domain;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
  Page<Doctor> findByDoctorNameContainingIgnoreCaseOrSpecialtyContainingIgnoreCase(String name, String specialty, Pageable pageable);
}
