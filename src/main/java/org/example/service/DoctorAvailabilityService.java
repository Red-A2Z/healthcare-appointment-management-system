package org.example.service;


import jakarta.transaction.Transactional;
import org.example.dto.DoctorAvailabilityDTOs.DoctorAvailabilityPatchRequestDTO;
import org.example.dto.DoctorAvailabilityDTOs.DoctorAvailabilityRequestDTO;
import org.example.dto.DoctorAvailabilityDTOs.DoctorAvailabilityResponseDTO;
import org.example.entity.Doctor;
import org.example.entity.DoctorAvailability;
import org.example.exception.InvalidDateRangeException;
import org.example.exception.ResourceNotFoundException;
import org.example.exception.TimePeriodAlreadyCoveredException;
import org.example.repository.DoctorAvailabilityRepository;
import org.example.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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


        DoctorAvailability newDoctorAvailability = new DoctorAvailability();
        newDoctorAvailability.setStartTime(doctorAvailabilityRequestDTO.getStartTime());
        newDoctorAvailability.setEndTime(doctorAvailabilityRequestDTO.getEndTime());
        newDoctorAvailability.setDoctor(doctor);

        List<DoctorAvailability> doctorAvailabilitiesList = doctorAvailabilityRepository.findAllByDoctorId(doctorId);


        List<DoctorAvailability> doctorAvailabilitiesToDelete = handlePeriodOverlapBeforeSaving(doctorAvailabilitiesList,newDoctorAvailability);

        deleteOldAndSaveNew(doctorAvailabilitiesToDelete, newDoctorAvailability);


        return mapToDoctorAvailabilityResponseDTO(newDoctorAvailability);
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



        boolean isStartTimePresent = doctorAvailabilityPatchRequestDTO.getStartTime().isPresent();
        boolean isEndTimePresent = doctorAvailabilityPatchRequestDTO.getEndTime().isPresent();


        if(isStartTimePresent){
            doctorAvailability.setStartTime(doctorAvailabilityPatchRequestDTO.getStartTime().get());
        }

        if(isEndTimePresent){
            doctorAvailability.setEndTime(doctorAvailabilityPatchRequestDTO.getEndTime().get());
        }


        if((!isStartTimePresent && isEndTimePresent)
                || (!isEndTimePresent && isStartTimePresent) ){

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





    private List<DoctorAvailability> handlePeriodOverlapBeforeSaving(List<DoctorAvailability> doctorAvailabilitiesList,DoctorAvailability newDoctorAvailability){


        List<DoctorAvailability> doctorAvailabilitiesToDelete = new ArrayList<>();


        for(DoctorAvailability element:doctorAvailabilitiesList){


            boolean isBeforeOrEqual_element = element.getStartTime().isBefore(newDoctorAvailability.getStartTime()) || element.getStartTime().isEqual(newDoctorAvailability.getStartTime());
            boolean isAfterOrEqual_element = element.getEndTime().isAfter(newDoctorAvailability.getEndTime()) || element.getEndTime().isEqual(newDoctorAvailability.getEndTime());

            if(isBeforeOrEqual_element && isAfterOrEqual_element){ // newDoctorAvailability is 'inside' element
                throw new TimePeriodAlreadyCoveredException("The provided period is already covered by an existing larger one");
            }


            boolean isBeforeOrEqual_new = newDoctorAvailability.getStartTime().isBefore(element.getStartTime()) || newDoctorAvailability.getStartTime().isEqual(element.getStartTime());
            boolean isAfterOrEqual_new = newDoctorAvailability.getEndTime().isAfter(element.getEndTime()) || newDoctorAvailability.getEndTime().isEqual(element.getEndTime());

            if(isBeforeOrEqual_new && isAfterOrEqual_new){ // element is 'inside' newDoctorAvailability

                doctorAvailabilitiesList.remove(element);
                doctorAvailabilitiesToDelete.add(element);

            }

        }


        // Try merging newDoctorAvailability with an existing doctorAvailability, then try merging the result of this merge


        doctorAvailabilitiesList.add(newDoctorAvailability);

        doctorAvailabilitiesList.sort(Comparator.comparing(DoctorAvailability::getStartTime));

        int index = doctorAvailabilitiesList.indexOf(newDoctorAvailability);

        List<DoctorAvailability> truncatedList= doctorAvailabilitiesList.subList(Math.max(0,index-1),Math.min(index+2,doctorAvailabilitiesList.size()));



        for(int i=0;i<truncatedList.size()-1;i++){
            DoctorAvailability element_a = doctorAvailabilitiesList.get(i);
            DoctorAvailability element_b = doctorAvailabilitiesList.get(i+1);

            if(element_a.getEndTime().isAfter(element_b.getStartTime()) || element_a.getEndTime().isEqual(element_b.getStartTime())){

                if(element_a== newDoctorAvailability){
                    doctorAvailabilitiesToDelete.add(element_b);
                    newDoctorAvailability.setEndTime(element_b.getEndTime());

                } else if (element_b == newDoctorAvailability) {
                    doctorAvailabilitiesToDelete.add(element_a);
                    newDoctorAvailability.setStartTime(element_a.getStartTime());

                }

            }

        }

        return doctorAvailabilitiesToDelete;


    }



    @Transactional
    private void deleteOldAndSaveNew(List<DoctorAvailability> doctorAvailabilitiesToDelete, DoctorAvailability newDoctorAvailability){


        for(DoctorAvailability doctorAvailabilityToDelete: doctorAvailabilitiesToDelete){
            doctorAvailabilityRepository.delete(doctorAvailabilityToDelete);
        }

        doctorAvailabilityRepository.save(newDoctorAvailability);



    }










}
