package com.mrsystem.domain;

import com.mrsystem.common.AuditableEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "pharmacies")
public class Pharmacy extends AuditableEntity {
  @Column(name = "pharmacy_name", nullable = false)
  private String pharmacyName;
  private String owner;
  private String address;
  private String contact;
  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "territory_id")
  private Territory territory;

  public String getPharmacyName() { return pharmacyName; }
  public void setPharmacyName(String pharmacyName) { this.pharmacyName = pharmacyName; }
  public String getOwner() { return owner; }
  public void setOwner(String owner) { this.owner = owner; }
  public String getAddress() { return address; }
  public void setAddress(String address) { this.address = address; }
  public String getContact() { return contact; }
  public void setContact(String contact) { this.contact = contact; }
  public Territory getTerritory() { return territory; }
  public void setTerritory(Territory territory) { this.territory = territory; }
}
