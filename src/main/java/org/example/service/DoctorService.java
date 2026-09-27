package org.example.service;

import jakarta.transaction.Transactional;
import org.example.dto.DoctorDTOs.DoctorPatchRequestDTO;
import org.example.dto.DoctorDTOs.DoctorRequestDTO;
import org.example.dto.DoctorDTOs.DoctorResponseDTO;
import org.example.entity.Appointment;
import org.example.entity.Doctor;
import org.example.enums.AppointmentStatus;
import org.example.exception.ConflictExcpetions.children.DuplicateResourceException;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.AppointmentRepository;
import org.example.repository.DoctorRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;


    public DoctorService(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public DoctorResponseDTO createDoctor(DoctorRequestDTO doctorRequestDTO){

        if(doctorRepository.existsByEmail(doctorRequestDTO.getEmail())){
            throw new DuplicateResourceException("Email "+ doctorRequestDTO.getEmail() +" already exists");
        }

        if(doctorRepository.existsByPhoneNumber(doctorRequestDTO.getPhoneNumber())){
            throw new DuplicateResourceException("Phone number"+ doctorRequestDTO.getPhoneNumber() +" already exists");
        }

        Doctor doctor = mapToEntity(doctorRequestDTO);

        Doctor savedDoctor = doctorRepository.save(doctor);

        return mapToDoctorResponseDTO(savedDoctor);

    }


    public DoctorResponseDTO readDoctor(Long id){

        Doctor doctor =  doctorRepository.findById(id).orElseThrow(()-> new ResourceAccessException("No doctor found for id: "+ id));

        return mapToDoctorResponseDTO(doctor);

    }



    public DoctorResponseDTO updateDoctor(Long id, DoctorRequestDTO doctorRequestDTO){

        if(doctorRepository.existsByEmail(doctorRequestDTO.getEmail())){
            throw new DuplicateResourceException("Email "+ doctorRequestDTO.getEmail() +" already exists");
        }

        if(doctorRepository.existsByPhoneNumber(doctorRequestDTO.getPhoneNumber())){
            throw new DuplicateResourceException("Phone number"+ doctorRequestDTO.getPhoneNumber() +" already exists");
        }


        Doctor doctor = doctorRepository.findById(id).orElseThrow(()-> new ResourceAccessException("No doctor found for id: "+ id));


        int i = updateAllFieldsExceptId(doctor, doctorRequestDTO);
        if(i==0){
            doctorRepository.save(doctor);
        }

        return mapToDoctorResponseDTO(doctor);
    }






    public DoctorResponseDTO updateSomeFieldsForDoctor(Long id, DoctorPatchRequestDTO doctorPatchRequestDTO){

        if(doctorPatchRequestDTO.getSpecialty().isPresent()){
            if(doctorRepository.existsByEmail(doctorPatchRequestDTO.getEmail().get())){
                throw new DuplicateResourceException("Email "+ doctorPatchRequestDTO.getEmail().get() +" already exists");
            }
        }
        if(doctorPatchRequestDTO.getPhoneNumber().isPresent()){
            if(doctorRepository.existsByPhoneNumber(doctorPatchRequestDTO.getPhoneNumber().get())){
                throw new DuplicateResourceException("Phone number"+ doctorPatchRequestDTO.getPhoneNumber().get() +" already exists");
            }
        }


        Doctor doctor = doctorRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No doctor found for id: "+ id));

        int i = updateSomeFields(doctor,doctorPatchRequestDTO);
        if(i==0){
            doctorRepository.save(doctor);
        }

        return mapToDoctorResponseDTO(doctor);


    }
















    private Doctor mapToEntity(DoctorRequestDTO doctorRequestDTO){

        Doctor doctor =  new Doctor();

        doctor.setFirstName(doctorRequestDTO.getFirstName());
        doctor.setLastName(doctorRequestDTO.getLastName());
        doctor.setSpecialty(doctorRequestDTO.getSpecialty());
        doctor.setPhoneNumber(doctorRequestDTO.getPhoneNumber());
        doctor.setEmail(doctorRequestDTO.getEmail());
        doctor.setIsActive(doctorRequestDTO.getIsActive());

        return doctor;

    }



    private DoctorResponseDTO mapToDoctorResponseDTO(Doctor doctor){

        DoctorResponseDTO doctorResponseDTO = new DoctorResponseDTO();

        doctorResponseDTO.setId(doctor.getId());
        doctorResponseDTO.setFirstName(doctor.getFirstName());
        doctorResponseDTO.setLastName(doctor.getLastName());
        doctorResponseDTO.setSpecialty(doctor.getSpecialty());
        doctorResponseDTO.setPhoneNumber(doctor.getPhoneNumber());
        doctorResponseDTO.setEmail(doctor.getEmail());
        doctorResponseDTO.setIsActive(doctor.getIsActive());
        doctorResponseDTO.setCreatedAt(doctor.getCreatedAt());


        return doctorResponseDTO;
    }



    private int updateAllFieldsExceptId(Doctor doctor, DoctorRequestDTO doctorRequestDTO){


        doctor.setFirstName(doctorRequestDTO.getFirstName());
        doctor.setLastName(doctorRequestDTO.getLastName());
        doctor.setSpecialty(doctorRequestDTO.getSpecialty());
        doctor.setPhoneNumber(doctorRequestDTO.getPhoneNumber());
        doctor.setEmail(doctorRequestDTO.getEmail());

        if(doctor.getIsActive().equals(true) && doctorRequestDTO.getIsActive().equals(false)){
            deactivateDoctor(doctor);
            return 1;
        }else {
            doctor.setIsActive(doctorRequestDTO.getIsActive());
        }

        return 0;

    }




    private int updateSomeFields(Doctor doctor, DoctorPatchRequestDTO doctorPatchRequestDTO){

        if(doctorPatchRequestDTO.getFirstName().isPresent()){
            doctor.setFirstName(doctorPatchRequestDTO.getFirstName().get());
        }
        if(doctorPatchRequestDTO.getLastName().isPresent()){
            doctor.setLastName(doctorPatchRequestDTO.getLastName().get());
        }
        if(doctorPatchRequestDTO.getSpecialty().isPresent()){
            doctor.setSpecialty(doctorPatchRequestDTO.getSpecialty().get());
        }
        if(doctorPatchRequestDTO.getPhoneNumber().isPresent()){
            doctor.setPhoneNumber(doctorPatchRequestDTO.getPhoneNumber().get());
        }
        if(doctorPatchRequestDTO.getEmail().isPresent()){
            doctor.setEmail(doctorPatchRequestDTO.getEmail().get());
        }
        if(doctorPatchRequestDTO.getIsActive().isPresent()){

            if(doctor.getIsActive().equals(true) && doctorPatchRequestDTO.getIsActive().get().equals(false)){

                deactivateDoctor(doctor);
                return 1;

            }else{
                doctor.setIsActive(doctorPatchRequestDTO.getIsActive().get());
            }

        }
        return 0;

    }


    @Transactional
    public void deactivateDoctor(Doctor doctor){

        Specification<Appointment> spec = Specification.unrestricted();
        spec = spec.and((root,query,builder)-> builder.equal(root.get("doctorId"),doctor.getId()));
        spec = spec.and((root,query,builder)-> builder.equal(root.get("appointmentStatus"), AppointmentStatus.SCHEDULED));
        spec = spec.and((root,query,builder)-> builder.greaterThan(root.get("startTime"), LocalDateTime.now()));

        List<Appointment> appointmentsToCancel = appointmentRepository.findAll(spec);

        appointmentRepository.deleteAll(appointmentsToCancel);
        doctor.setIsActive(false);
        doctorRepository.save(doctor);

    }


}
