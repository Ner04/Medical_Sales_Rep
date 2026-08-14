package com.mrsystem.dashboard;

import com.mrsystem.domain.*;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
  private final UserRepository users;
  private final DoctorRepository doctors;
  private final HospitalRepository hospitals;
  private final PharmacyRepository pharmacies;
  private final VisitRepository visits;
  private final UserRepository userRepository;

  public DashboardController(UserRepository users, DoctorRepository doctors, HospitalRepository hospitals,
                             PharmacyRepository pharmacies, VisitRepository visits, UserRepository userRepository) {
    this.users = users;
    this.doctors = doctors;
    this.hospitals = hospitals;
    this.pharmacies = pharmacies;
    this.visits = visits;
    this.userRepository = userRepository;
  }

  @GetMapping("/admin")
  public Map<String, Object> admin() {
    LocalDate today = LocalDate.now();
    return Map.of(
        "kpis", List.of(
            Map.of("label", "Total MRs", "value", users.count()),
            Map.of("label", "Active MRs Today", "value", users.countByEnabledTrue()),
            Map.of("label", "Planned Visits Today", "value", visits.countByVisitDateAndStatus(today, "PLANNED")),
            Map.of("label", "Completed Visits Today", "value", visits.countByVisitDateAndStatus(today, "COMPLETED")),
            Map.of("label", "Missed Visits", "value", visits.countByVisitDateAndStatus(today, "MISSED")),
            Map.of("label", "Total Doctors", "value", doctors.count()),
            Map.of("label", "Total Hospitals", "value", hospitals.count()),
            Map.of("label", "Total Pharmacies", "value", pharmacies.count()),
            Map.of("label", "Total Orders", "value", 0),
            Map.of("label", "Revenue Generated", "value", "0"),
            Map.of("label", "Monthly Growth %", "value", "12.4%"),
            Map.of("label", "Territory Coverage %", "value", "78%")),
        "dailyVisitTrend", List.of(18, 24, 22, 31, 27, 35, 29),
        "territoryPerformance", List.of(
            Map.of("territory", "Mumbai Central", "coverage", 78, "visits", visits.countByVisitDate(today))),
        "activity", List.of(
            "New Visit Logged", "New Doctor Added", "Leave Request", "Expense Request", "Tour Plan Approval"));
  }

  @GetMapping("/mr")
  public Map<String, Object> mr(Principal principal) {
    User user = userRepository.findByUsernameIgnoreCase(principal.getName()).orElseThrow();
    LocalDate today = LocalDate.now();
    return Map.of(
        "name", user.getName(),
        "todayVisits", visits.countByMrIdAndVisitDate(user.getId(), today),
        "upcomingVisits", 4,
        "pendingVisits", 2,
        "attendanceStatus", "Not checked in",
        "monthlyTarget", 120,
        "currentAchievement", 72,
        "incentiveEstimate", 18500,
        "notifications", List.of("Visit reminder: Dr. Asha Mehta", "Tour plan pending approval"),
        "recommendations", List.of("High-value doctor not visited recently", "Coverage gap in Fort pin code 400001"));
  }
}
