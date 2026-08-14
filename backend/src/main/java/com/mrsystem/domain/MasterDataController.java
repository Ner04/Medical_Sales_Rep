package com.mrsystem.domain;

import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/master")
public class MasterDataController {
  private final TerritoryRepository territories;
  private final DoctorRepository doctors;
  private final HospitalRepository hospitals;
  private final PharmacyRepository pharmacies;

  public MasterDataController(TerritoryRepository territories, DoctorRepository doctors,
                              HospitalRepository hospitals, PharmacyRepository pharmacies) {
    this.territories = territories;
    this.doctors = doctors;
    this.hospitals = hospitals;
    this.pharmacies = pharmacies;
  }

  @GetMapping("/territories")
  public Page<Territory> territories(Pageable pageable) { return territories.findAll(pageable); }
  @PostMapping("/territories") @PreAuthorize("hasAuthority('MANAGE_TERRITORIES')")
  public Territory createTerritory(@Valid @RequestBody Territory territory) { return territories.save(territory); }
  @PutMapping("/territories/{id}") @PreAuthorize("hasAuthority('MANAGE_TERRITORIES')")
  public Territory updateTerritory(@PathVariable UUID id, @Valid @RequestBody Territory input) {
    Territory t = territories.findById(id).orElseThrow();
    t.setTerritoryName(input.getTerritoryName()); t.setRegion(input.getRegion()); t.setState(input.getState());
    t.setCity(input.getCity()); t.setPinCodes(input.getPinCodes()); t.setActive(input.isActive());
    return territories.save(t);
  }

  @GetMapping("/doctors")
  public Page<Doctor> doctors(@RequestParam(defaultValue = "") String q, Pageable pageable) {
    return q.isBlank() ? doctors.findAll(pageable) : doctors.findByDoctorNameContainingIgnoreCaseOrSpecialtyContainingIgnoreCase(q, q, pageable);
  }
  @PostMapping("/doctors") @PreAuthorize("hasAuthority('MANAGE_DOCTORS')")
  public Doctor createDoctor(@Valid @RequestBody Doctor doctor) { return doctors.save(doctor); }

  @GetMapping("/hospitals")
  public Page<Hospital> hospitals(Pageable pageable) { return hospitals.findAll(pageable); }
  @PostMapping("/hospitals") @PreAuthorize("hasAuthority('MANAGE_HOSPITALS')")
  public Hospital createHospital(@Valid @RequestBody Hospital hospital) { return hospitals.save(hospital); }

  @GetMapping("/pharmacies")
  public Page<Pharmacy> pharmacies(Pageable pageable) { return pharmacies.findAll(pageable); }
  @PostMapping("/pharmacies") @PreAuthorize("hasAuthority('MANAGE_PHARMACIES')")
  public Pharmacy createPharmacy(@Valid @RequestBody Pharmacy pharmacy) { return pharmacies.save(pharmacy); }
}
