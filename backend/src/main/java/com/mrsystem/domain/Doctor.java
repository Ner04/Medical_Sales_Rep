package com.mrsystem.domain;

import com.mrsystem.common.AuditableEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "doctors")
public class Doctor extends AuditableEntity {
  @Column(name = "doctor_name", nullable = false)
  private String doctorName;
  private String specialty;
  private String qualification;
  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "hospital_id")
  private Hospital hospital;
  private String clinic;
  private String address;
  private String phone;
  private String email;
  private String category;
  @Column(name = "potential_score")
  private Integer potentialScore = 0;
  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "territory_id")
  private Territory territory;
  private BigDecimal latitude;
  private BigDecimal longitude;
  private String notes;

  public String getDoctorName() { return doctorName; }
  public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
  public String getSpecialty() { return specialty; }
  public void setSpecialty(String specialty) { this.specialty = specialty; }
  public String getQualification() { return qualification; }
  public void setQualification(String qualification) { this.qualification = qualification; }
  public Hospital getHospital() { return hospital; }
  public void setHospital(Hospital hospital) { this.hospital = hospital; }
  public String getClinic() { return clinic; }
  public void setClinic(String clinic) { this.clinic = clinic; }
  public String getAddress() { return address; }
  public void setAddress(String address) { this.address = address; }
  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getCategory() { return category; }
  public void setCategory(String category) { this.category = category; }
  public Integer getPotentialScore() { return potentialScore; }
  public void setPotentialScore(Integer potentialScore) { this.potentialScore = potentialScore; }
  public Territory getTerritory() { return territory; }
  public void setTerritory(Territory territory) { this.territory = territory; }
  public BigDecimal getLatitude() { return latitude; }
  public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
  public BigDecimal getLongitude() { return longitude; }
  public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
  public String getNotes() { return notes; }
  public void setNotes(String notes) { this.notes = notes; }
}
