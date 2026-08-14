package com.mrsystem.domain;

import com.mrsystem.common.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "territories")
public class Territory extends AuditableEntity {
  @Column(name = "territory_name", nullable = false)
  private String territoryName;
  private String region;
  private String state;
  private String city;
  @Column(name = "pin_codes")
  private String pinCodes;
  private boolean active = true;

  public String getTerritoryName() { return territoryName; }
  public void setTerritoryName(String territoryName) { this.territoryName = territoryName; }
  public String getRegion() { return region; }
  public void setRegion(String region) { this.region = region; }
  public String getState() { return state; }
  public void setState(String state) { this.state = state; }
  public String getCity() { return city; }
  public void setCity(String city) { this.city = city; }
  public String getPinCodes() { return pinCodes; }
  public void setPinCodes(String pinCodes) { this.pinCodes = pinCodes; }
  public boolean isActive() { return active; }
  public void setActive(boolean active) { this.active = active; }
}
