package com.mrsystem.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/visits")
public class VisitController {
  private final VisitRepository visits;
  private final UserRepository users;
  private final DoctorRepository doctors;

  public VisitController(VisitRepository visits, UserRepository users, DoctorRepository doctors) {
    this.visits = visits;
    this.users = users;
    this.doctors = doctors;
  }

  @GetMapping
  public List<Visit> list() { return visits.findAll(); }

  @PostMapping
  @PreAuthorize("hasAuthority('CREATE_VISIT')")
  public Visit schedule(Principal principal, @Valid @RequestBody VisitRequest request) {
    Visit visit = new Visit();
    visit.setMr(users.findByUsernameIgnoreCase(principal.getName()).orElseThrow());
    if (request.doctorId != null) visit.setDoctor(doctors.findById(request.doctorId).orElseThrow());
    visit.setVisitDate(request.visitDate);
    visit.setPlannedStart(request.plannedStart);
    visit.setDiscussionNotes(request.notes);
    visit.setStatus("PLANNED");
    return visits.save(visit);
  }

  @PostMapping("/{id}/start")
  @PreAuthorize("hasAuthority('START_VISIT')")
  public Visit start(@PathVariable UUID id, @RequestBody GeoPoint point) {
    Visit visit = visits.findById(id).orElseThrow();
    visit.setCheckInTime(Instant.now());
    visit.setCheckInLatitude(point.latitude);
    visit.setCheckInLongitude(point.longitude);
    return visits.save(visit);
  }

  @PostMapping("/{id}/end")
  @PreAuthorize("hasAuthority('END_VISIT')")
  public Visit end(@PathVariable UUID id, @RequestBody VisitCompletion completion) {
    Visit visit = visits.findById(id).orElseThrow();
    visit.setCheckOutTime(Instant.now());
    visit.setCheckOutLatitude(completion.latitude);
    visit.setCheckOutLongitude(completion.longitude);
    visit.setDoctorMet(completion.doctorMet);
    visit.setDiscussionNotes(completion.notes);
    visit.setProductsDiscussed(completion.productsDiscussed);
    visit.setSamplesGiven(completion.samplesGiven);
    visit.setStatus("COMPLETED");
    return visits.save(visit);
  }

  @PostMapping("/route/optimize")
  @PreAuthorize("hasAuthority('VIEW_ROUTE')")
  public List<RouteStop> optimizeRoute(@RequestBody List<RouteStop> stops) {
    if (stops.isEmpty()) return stops;
    RouteStop origin = stops.get(0);
    return stops.stream()
        .sorted(Comparator.comparing(stop -> distance(origin.latitude, origin.longitude, stop.latitude, stop.longitude)))
        .toList();
  }

  private double distance(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {
    double x = lat1.doubleValue() - lat2.doubleValue();
    double y = lon1.doubleValue() - lon2.doubleValue();
    return Math.sqrt(x * x + y * y);
  }

  public static class VisitRequest {
    public UUID doctorId;
    @NotNull public LocalDate visitDate;
    public Instant plannedStart;
    public String notes;
  }
  public static class GeoPoint {
    public BigDecimal latitude;
    public BigDecimal longitude;
  }
  public static class VisitCompletion extends GeoPoint {
    public boolean doctorMet;
    public String notes;
    public String productsDiscussed;
    public String samplesGiven;
  }
  public record RouteStop(UUID id, String name, BigDecimal latitude, BigDecimal longitude) {}
}
