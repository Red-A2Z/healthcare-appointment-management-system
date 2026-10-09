package org.example.service;


import jakarta.transaction.Transactional;
import org.example.entity.Appointment;
import org.example.entity.Doctor;
import org.example.enums.AppointmentStatus;
import org.example.repository.AppointmentRepository;
import org.example.repository.DoctorRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DoctorTransactionalService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;


    public DoctorTransactionalService(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
    }



    @Transactional
    public void deactivateDoctor(Doctor doctor){

        Specification<Appointment> spec = Specification.unrestricted();
        spec = spec.and((root,query,builder)-> builder.equal(root.get("doctor").get("id"),doctor.getId()));
        spec = spec.and((root,query,builder)-> builder.equal(root.get("appointmentStatus"), AppointmentStatus.SCHEDULED));
        spec = spec.and((root,query,builder)-> builder.greaterThan(root.get("startTime"), LocalDateTime.now()));

        List<Appointment> appointmentsToCancel = appointmentRepository.findAll(spec);

        appointmentRepository.deleteAll(appointmentsToCancel);
        doctor.setIsActive(false);
        doctorRepository.save(doctor);

    }


}
