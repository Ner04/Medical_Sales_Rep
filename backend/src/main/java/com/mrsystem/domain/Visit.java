package com.mrsystem.domain;

import com.mrsystem.common.AuditableEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "visits")
public class Visit extends AuditableEntity {
  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "mr_id", nullable = false)
  private User mr;
  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "doctor_id")
  private Doctor doctor;
  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "pharmacy_id")
  private Pharmacy pharmacy;
  @Column(name = "visit_date", nullable = false)
  private LocalDate visitDate;
  @Column(name = "planned_start")
  private Instant plannedStart;
  @Column(name = "check_in_time")
  private Instant checkInTime;
  @Column(name = "check_out_time")
  private Instant checkOutTime;
  @Column(name = "check_in_latitude")
  private BigDecimal checkInLatitude;
  @Column(name = "check_in_longitude")
  private BigDecimal checkInLongitude;
  @Column(name = "check_out_latitude")
  private BigDecimal checkOutLatitude;
  @Column(name = "check_out_longitude")
  private BigDecimal checkOutLongitude;
  @Column(name = "distance_from_site_meters")
  private BigDecimal distanceFromSiteMeters;
  @Column(name = "doctor_met")
  private boolean doctorMet;
  @Column(name = "discussion_notes")
  private String discussionNotes;
  @Column(name = "products_discussed")
  private String productsDiscussed;
  @Column(name = "samples_given")
  private String samplesGiven;
  @Column(name = "photo_url")
  private String photoUrl;
  private String status = "PLANNED";

  public User getMr() { return mr; }
  public void setMr(User mr) { this.mr = mr; }
  public Doctor getDoctor() { return doctor; }
  public void setDoctor(Doctor doctor) { this.doctor = doctor; }
  public Pharmacy getPharmacy() { return pharmacy; }
  public void setPharmacy(Pharmacy pharmacy) { this.pharmacy = pharmacy; }
  public LocalDate getVisitDate() { return visitDate; }
  public void setVisitDate(LocalDate visitDate) { this.visitDate = visitDate; }
  public Instant getPlannedStart() { return plannedStart; }
  public void setPlannedStart(Instant plannedStart) { this.plannedStart = plannedStart; }
  public Instant getCheckInTime() { return checkInTime; }
  public void setCheckInTime(Instant checkInTime) { this.checkInTime = checkInTime; }
  public Instant getCheckOutTime() { return checkOutTime; }
  public void setCheckOutTime(Instant checkOutTime) { this.checkOutTime = checkOutTime; }
  public BigDecimal getCheckInLatitude() { return checkInLatitude; }
  public void setCheckInLatitude(BigDecimal checkInLatitude) { this.checkInLatitude = checkInLatitude; }
  public BigDecimal getCheckInLongitude() { return checkInLongitude; }
  public void setCheckInLongitude(BigDecimal checkInLongitude) { this.checkInLongitude = checkInLongitude; }
  public BigDecimal getCheckOutLatitude() { return checkOutLatitude; }
  public void setCheckOutLatitude(BigDecimal checkOutLatitude) { this.checkOutLatitude = checkOutLatitude; }
  public BigDecimal getCheckOutLongitude() { return checkOutLongitude; }
  public void setCheckOutLongitude(BigDecimal checkOutLongitude) { this.checkOutLongitude = checkOutLongitude; }
  public BigDecimal getDistanceFromSiteMeters() { return distanceFromSiteMeters; }
  public void setDistanceFromSiteMeters(BigDecimal distanceFromSiteMeters) { this.distanceFromSiteMeters = distanceFromSiteMeters; }
  public boolean isDoctorMet() { return doctorMet; }
  public void setDoctorMet(boolean doctorMet) { this.doctorMet = doctorMet; }
  public String getDiscussionNotes() { return discussionNotes; }
  public void setDiscussionNotes(String discussionNotes) { this.discussionNotes = discussionNotes; }
  public String getProductsDiscussed() { return productsDiscussed; }
  public void setProductsDiscussed(String productsDiscussed) { this.productsDiscussed = productsDiscussed; }
  public String getSamplesGiven() { return samplesGiven; }
  public void setSamplesGiven(String samplesGiven) { this.samplesGiven = samplesGiven; }
  public String getPhotoUrl() { return photoUrl; }
  public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
}
