package org.example.service;

import jakarta.transaction.Transactional;
import org.example.entity.DoctorAvailability;
import org.example.repository.DoctorAvailabilityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorAvailabilityTransactionalService {


    private final DoctorAvailabilityRepository doctorAvailabilityRepository;

    public DoctorAvailabilityTransactionalService(DoctorAvailabilityRepository doctorAvailabilityRepository) {
        this.doctorAvailabilityRepository = doctorAvailabilityRepository;
    }



    @Transactional
    public void deleteOldAndSaveNew(List<DoctorAvailability> doctorAvailabilitiesToDelete, DoctorAvailability newDoctorAvailability){


        for(DoctorAvailability doctorAvailabilityToDelete: doctorAvailabilitiesToDelete){
            doctorAvailabilityRepository.delete(doctorAvailabilityToDelete);
        }

        doctorAvailabilityRepository.save(newDoctorAvailability);

    }



}
