package com.mrsystem.domain;

import com.mrsystem.common.AuditableEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "hospitals")
public class Hospital extends AuditableEntity {
  @Column(nullable = false)
  private String name;
  private String address;
  @Column(name = "contact_person")
  private String contactPerson;
  private String phone;
  private String email;
  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "territory_id")
  private Territory territory;

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getAddress() { return address; }
  public void setAddress(String address) { this.address = address; }
  public String getContactPerson() { return contactPerson; }
  public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public Territory getTerritory() { return territory; }
  public void setTerritory(Territory territory) { this.territory = territory; }
}
