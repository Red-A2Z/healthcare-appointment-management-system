package org.example.repository;

import org.example.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment,Long> , JpaSpecificationExecutor<Appointment> {

    List<Appointment> findAllByPatientId(Long patientId);
}
