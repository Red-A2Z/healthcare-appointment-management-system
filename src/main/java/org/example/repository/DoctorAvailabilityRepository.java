package org.example.repository;

import org.example.entity.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability,Long> {


    List<DoctorAvailability> findAllByDoctorId(Long doctorId);

}
