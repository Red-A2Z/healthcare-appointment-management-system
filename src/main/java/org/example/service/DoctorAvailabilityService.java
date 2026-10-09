package org.example.service;


import org.example.dto.DoctorAvailabilityDTOs.DoctorAvailabilityListResponseDTO;
import org.example.dto.DoctorAvailabilityDTOs.DoctorAvailabilityPatchRequestDTO;
import org.example.dto.DoctorAvailabilityDTOs.DoctorAvailabilityRequestDTO;
import org.example.dto.DoctorAvailabilityDTOs.DoctorAvailabilityResponseDTO;
import org.example.entity.Doctor;
import org.example.entity.DoctorAvailability;
import org.example.exception.ConflictExcpetions.children.DoctorInactiveException;
import org.example.exception.InvalidDateRangeException;
import org.example.exception.ResourceNotFoundException;
import org.example.exception.ConflictExcpetions.children.TimePeriodAlreadyCoveredException;
import org.example.repository.DoctorAvailabilityRepository;
import org.example.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class DoctorAvailabilityService {

    DoctorAvailabilityTransactionalService doctorAvailabilityTransactionalService;
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


        // Doctor must be active
        if(doctor.getIsActive().equals(false)){
            throw new DoctorInactiveException("Doctor with id: "+doctorId+" is inactive");
        }

        DoctorAvailability newDoctorAvailability = new DoctorAvailability();
        newDoctorAvailability.setStartTime(doctorAvailabilityRequestDTO.getStartTime());
        newDoctorAvailability.setEndTime(doctorAvailabilityRequestDTO.getEndTime());
        newDoctorAvailability.setDoctor(doctor);

        List<DoctorAvailability> doctorAvailabilitiesList = doctorAvailabilityRepository.findAllByDoctorId(doctorId);


        List<DoctorAvailability> doctorAvailabilitiesToDelete = handlePeriodOverlapBeforeSaving(doctorAvailabilitiesList,newDoctorAvailability);

        doctorAvailabilityTransactionalService.deleteOldAndSaveNew(doctorAvailabilitiesToDelete, newDoctorAvailability);


        return mapToDoctorAvailabilityResponseDTO(newDoctorAvailability);
    }



    public DoctorAvailabilityResponseDTO readDoctorAvailability(Long id){

        DoctorAvailability doctorAvailability = doctorAvailabilityRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No doctor availability found for id: "+id));

        return mapToDoctorAvailabilityResponseDTO(doctorAvailability);
    }


    public DoctorAvailabilityListResponseDTO readAllDoctorAvailabilities(Long doctorId){

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(()-> new ResourceNotFoundException("No doctor found for id: "+doctorId));


        List<DoctorAvailability> doctorAvailabilityList = doctorAvailabilityRepository.findAllByDoctorId(doctorId);

        DoctorAvailabilityListResponseDTO doctorAvailabilityListResponseDTO = new DoctorAvailabilityListResponseDTO();
        doctorAvailabilityListResponseDTO.setDoctorId(doctorId);
        doctorAvailabilityListResponseDTO.setDoctorActive(doctor.getIsActive());
        doctorAvailabilityListResponseDTO.setDoctorAvailabilityList(doctorAvailabilityList);

        return doctorAvailabilityListResponseDTO;
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



        List<DoctorAvailability> doctorAvailabilitiesList = doctorAvailabilityRepository.findAllByDoctorId(doctorId);

        // Remove the object of the record we want to update
        doctorAvailabilitiesList.removeIf(obj -> obj.getId().equals(id));

        List<DoctorAvailability> doctorAvailabilitiesToDelete = handlePeriodOverlapBeforeSaving(doctorAvailabilitiesList,doctorAvailability);

        doctorAvailabilityTransactionalService.deleteOldAndSaveNew(doctorAvailabilitiesToDelete, doctorAvailability);



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

            if(!doctorAvailability.getStartTime().isBefore(doctorAvailability.getEndTime())){

                throw new InvalidDateRangeException("Start time must be before end time. Consider checking the already saved values");

            }
        }



        List<DoctorAvailability> doctorAvailabilitiesList = doctorAvailabilityRepository.findAllByDoctorId(doctorAvailability.getDoctor().getId());
        // Remove the object of the record we want to update
        doctorAvailabilitiesList.removeIf(obj -> obj.getId().equals(id));

        List<DoctorAvailability> doctorAvailabilitiesToDelete = handlePeriodOverlapBeforeSaving(doctorAvailabilitiesList,doctorAvailability);

        doctorAvailabilityTransactionalService.deleteOldAndSaveNew(doctorAvailabilitiesToDelete, doctorAvailability);



        return mapToDoctorAvailabilityResponseDTO(doctorAvailability);


    }








    private DoctorAvailabilityResponseDTO mapToDoctorAvailabilityResponseDTO(DoctorAvailability doctorAvailability){


        DoctorAvailabilityResponseDTO doctorAvailabilityResponseDTO = new DoctorAvailabilityResponseDTO();
        doctorAvailabilityResponseDTO.setId(doctorAvailability.getId());
        doctorAvailabilityResponseDTO.setDoctorId(doctorAvailability.getDoctor().getId());
        doctorAvailabilityResponseDTO.setStartTime(doctorAvailability.getStartTime());
        doctorAvailabilityResponseDTO.setEndTime(doctorAvailability.getEndTime());
        doctorAvailabilityResponseDTO.setCreatedAt(doctorAvailability.getCreatedAt());

        return doctorAvailabilityResponseDTO;


    }





    public List<DoctorAvailability> handlePeriodOverlapBeforeSaving(List<DoctorAvailability> doctorAvailabilitiesList,DoctorAvailability newDoctorAvailability){


        List<DoctorAvailability> doctorAvailabilitiesToDelete = new ArrayList<>();

        List<DoctorAvailability> listForLoop = new ArrayList<>(doctorAvailabilitiesList);

        for(DoctorAvailability element:listForLoop){


            boolean isBeforeOrEqual_element = !element.getStartTime().isAfter(newDoctorAvailability.getStartTime());
            boolean isAfterOrEqual_element = !element.getEndTime().isBefore(newDoctorAvailability.getEndTime());

            if(isBeforeOrEqual_element && isAfterOrEqual_element){ // newDoctorAvailability is 'inside' element
                throw new TimePeriodAlreadyCoveredException("The provided period is already covered by an existing one");
            }


            boolean isBeforeOrEqual_new = !newDoctorAvailability.getStartTime().isAfter(element.getStartTime());
            boolean isAfterOrEqual_new = !newDoctorAvailability.getEndTime().isBefore(element.getEndTime());

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
            DoctorAvailability element_a = truncatedList.get(i);
            DoctorAvailability element_b = truncatedList.get(i+1);

            if(!element_a.getEndTime().isBefore(element_b.getStartTime())){

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














}
