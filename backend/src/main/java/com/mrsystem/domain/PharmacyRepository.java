package com.mrsystem.domain;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyRepository extends JpaRepository<Pharmacy, UUID> {}
