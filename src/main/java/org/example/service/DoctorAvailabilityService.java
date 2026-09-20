package org.example.service;


import org.example.dto.DoctorAvailabilityDTOs.DoctorAvailabilityPatchRequestDTO;
import org.example.dto.DoctorAvailabilityDTOs.DoctorAvailabilityRequestDTO;
import org.example.dto.DoctorAvailabilityDTOs.DoctorAvailabilityResponseDTO;
import org.example.entity.Doctor;
import org.example.entity.DoctorAvailability;
import org.example.exception.InvalidDateRangeException;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.DoctorAvailabilityRepository;
import org.example.repository.DoctorRepository;
import org.springframework.stereotype.Service;

@Service
public class DoctorAvailabilityService {

    DoctorAvailabilityRepository doctorAvailabilityRepository;
    DoctorRepository doctorRepository;


    public DoctorAvailabilityService(DoctorAvailabilityRepository doctorAvailabilityRepository, DoctorRepository doctorRepository){
        this.doctorAvailabilityRepository = doctorAvailabilityRepository;
        this.doctorRepository = doctorRepository;

    }



    public DoctorAvailabilityResponseDTO createDoctorAvailability(DoctorAvailabilityRequestDTO doctorAvailabilityRequestDTO){

        Long doctorId = doctorAvailabilityRequestDTO.getDoctorId();
        Doctor doctor = doctorRepository.findById(doctorId)
                                        .orElseThrow(()-> new ResourceNotFoundException("No doctor found for id: "+doctorId));


        DoctorAvailability doctorAvailability = new DoctorAvailability();
        doctorAvailability.setStartTime(doctorAvailabilityRequestDTO.getStartTime());
        doctorAvailability.setEndTime(doctorAvailabilityRequestDTO.getEndTime());
        doctorAvailability.setDoctor(doctor);

        doctorAvailabilityRepository.save(doctorAvailability);


        return mapToDoctorAvailabilityResponseDTO(doctorAvailability);
    }



    public DoctorAvailabilityResponseDTO readDoctorAvailability(Long id){

        DoctorAvailability doctorAvailability = doctorAvailabilityRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No doctor availability found for id: "+id));

        return mapToDoctorAvailabilityResponseDTO(doctorAvailability);
    }



    public DoctorAvailabilityResponseDTO updateDoctorAvailability(Long id,
                                                                  DoctorAvailabilityRequestDTO doctorAvailabilityRequestDTO){


        DoctorAvailability doctorAvailability = doctorAvailabilityRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No doctor availability found for id: "+id));


        Long doctorId = doctorAvailabilityRequestDTO.getDoctorId();
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(()-> new ResourceNotFoundException("No doctor found for id: "+doctorId));


        doctorAvailability.setStartTime(doctorAvailabilityRequestDTO.getStartTime());
        doctorAvailability.setEndTime(doctorAvailabilityRequestDTO.getEndTime());
        doctorAvailability.setDoctor(doctor);

        doctorAvailabilityRepository.save(doctorAvailability);

        return mapToDoctorAvailabilityResponseDTO(doctorAvailability);


    }




    public void deleteDoctorAvailability(Long id){

        DoctorAvailability doctorAvailability = doctorAvailabilityRepository.findById(id)
                                                                            .orElseThrow(()-> new ResourceNotFoundException("No doctor availability found for id: "+id));

        doctorAvailabilityRepository.delete(doctorAvailability);


    }



    public DoctorAvailabilityResponseDTO updateSomeFieldsForDoctorAvailability(Long id,
                                                                               DoctorAvailabilityPatchRequestDTO doctorAvailabilityPatchRequestDTO){

        DoctorAvailability doctorAvailability = doctorAvailabilityRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No doctor availability found for id: "+id));




        if(doctorAvailabilityPatchRequestDTO.getDoctorId().isPresent()){

            Long doctorId = doctorAvailabilityPatchRequestDTO.getDoctorId().get();
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(()-> new ResourceNotFoundException("No doctor found for id: "+doctorId));

            doctorAvailability.setDoctor(doctor);
        }




        if(doctorAvailabilityPatchRequestDTO.getStartTime().isPresent()){
            doctorAvailability.setStartTime(doctorAvailabilityPatchRequestDTO.getStartTime().get());
        }

        if(doctorAvailabilityPatchRequestDTO.getEndTime().isPresent()){
            doctorAvailability.setEndTime(doctorAvailabilityPatchRequestDTO.getEndTime().get());
        }


        if(!doctorAvailabilityPatchRequestDTO.getStartTime().isPresent()
                || !doctorAvailabilityPatchRequestDTO.getEndTime().isPresent() ){

            if(doctorAvailability.getStartTime().isAfter(doctorAvailability.getEndTime())
                    || doctorAvailability.getStartTime().isEqual(doctorAvailability.getEndTime())){

                throw new InvalidDateRangeException("Start time shouldn't be after end time. Consider checking the already saved values");

            }
        }




        doctorAvailabilityRepository.save(doctorAvailability);

        return mapToDoctorAvailabilityResponseDTO(doctorAvailability);


    }












    private DoctorAvailabilityResponseDTO mapToDoctorAvailabilityResponseDTO(DoctorAvailability doctorAvailability){


        DoctorAvailabilityResponseDTO doctorAvailabilityResponseDTO = new DoctorAvailabilityResponseDTO();
        doctorAvailabilityResponseDTO.setId(doctorAvailability.getId());
        doctorAvailabilityResponseDTO.setDoctorId(doctorAvailability.getId());
        doctorAvailabilityResponseDTO.setStartTime(doctorAvailability.getStartTime());
        doctorAvailabilityResponseDTO.setEndTime(doctorAvailability.getEndTime());
        doctorAvailabilityResponseDTO.setCreatedAt(doctorAvailability.getCreatedAt());

        return doctorAvailabilityResponseDTO;


    }










}
