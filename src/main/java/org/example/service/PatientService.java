package org.example.service;


import org.example.dto.PatientDTOs.PatientPatchRequestDTO;
import org.example.dto.PatientDTOs.PatientRequestDTO;
import org.example.dto.PatientDTOs.PatientResponseDTO;
import org.example.entity.Patient;
import org.example.exception.DuplicateResourceException;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    PatientRepository patientRepository;


    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }




    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){

        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())){
            throw new DuplicateResourceException("Email "+ patientRequestDTO.getEmail() +" already exists");
        }

        if(patientRepository.existsByPhoneNumber(patientRequestDTO.getPhoneNumber())){
            throw new DuplicateResourceException("Phone number"+ patientRequestDTO.getPhoneNumber() +" already exists");
        }

        Patient patient = new Patient();

        mapToEntity(patient,patientRequestDTO);

        patientRepository.save(patient);

        return mapToPatientResponseDTO(patient);
    }




    public PatientResponseDTO readPatient(Long id){


        Patient patient  = patientRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No patient found for id: "+ id));

        return mapToPatientResponseDTO(patient);
    }




    public PatientResponseDTO updatePatient(Long id, PatientRequestDTO patientRequestDTO){


        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())){
            throw new DuplicateResourceException("Email "+ patientRequestDTO.getEmail() +" already exists");
        }

        if(patientRepository.existsByPhoneNumber(patientRequestDTO.getPhoneNumber())){
            throw new DuplicateResourceException("Phone number"+ patientRequestDTO.getPhoneNumber() +" already exists");
        }

        Patient patient  = patientRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No patient found for id: "+ id));

        mapToEntity(patient,patientRequestDTO);

        patientRepository.save(patient);


        return mapToPatientResponseDTO(patient);
    }




    public void deletePatient(Long id){

        Patient patient  = patientRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No patient found for id: "+ id));

        patientRepository.delete(patient);

    }





    public PatientResponseDTO updateSomeFieldsForPatient(Long id, PatientPatchRequestDTO patientPatchRequestDTO){


        if(patientPatchRequestDTO.getEmail().isPresent()){
            String email = patientPatchRequestDTO.getEmail().get();
            if(patientRepository.existsByEmail(email)){
                throw new DuplicateResourceException("Email "+ email +" already exists");
            }
        }


        if(patientPatchRequestDTO.getPhoneNumber().isPresent()){
            String phoneNumber = patientPatchRequestDTO.getPhoneNumber().get();
            if(patientRepository.existsByPhoneNumber(phoneNumber)){
                throw new DuplicateResourceException("Phone number"+ phoneNumber +" already exists");
            }

        }

        Patient patient  = patientRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No patient found for id: "+ id));


        mapToEntityForPatch(patient, patientPatchRequestDTO);

        patientRepository.save(patient);

        return mapToPatientResponseDTO(patient);
    }












    private void mapToEntity(Patient patient,PatientRequestDTO patientRequestDTO){

        patient.setFirstName(patientRequestDTO.getFirstName());
        patient.setLastName(patientRequestDTO.getLastName());
        patient.setDateOfBirth(patientRequestDTO.getDateOfBirth());
        patient.setPhoneNumber(patientRequestDTO.getPhoneNumber());
        patient.setEmail(patientRequestDTO.getEmail());

    }


    private PatientResponseDTO mapToPatientResponseDTO(Patient patient){

        PatientResponseDTO patientResponseDTO = new PatientResponseDTO();

        patientResponseDTO.setId(patient.getId());
        patientResponseDTO.setFirstName(patient.getFirstName());
        patientResponseDTO.setLastName(patient.getLastName());
        patientResponseDTO.setDateOfBirth(patient.getDateOfBirth());
        patientResponseDTO.setPhoneNumber(patient.getPhoneNumber());
        patientResponseDTO.setEmail(patient.getEmail());

        return patientResponseDTO;
    }




    private void mapToEntityForPatch(Patient patient, PatientPatchRequestDTO patientPatchRequestDTO){

        if(patientPatchRequestDTO.getFirstName().isPresent()){
            patient.setFirstName(patientPatchRequestDTO.getFirstName().get());
        }
        if(patientPatchRequestDTO.getLastName().isPresent()){
            patient.setLastName(patientPatchRequestDTO.getLastName().get());
        }
        if(patientPatchRequestDTO.getDateOfBirth().isPresent()){
            patient.setDateOfBirth(patientPatchRequestDTO.getDateOfBirth().get());
        }
        if(patientPatchRequestDTO.getPhoneNumber().isPresent()){
            patient.setPhoneNumber(patientPatchRequestDTO.getPhoneNumber().get());
        }
        if(patientPatchRequestDTO.getEmail().isPresent()){
            patient.setEmail(patientPatchRequestDTO.getEmail().get());
        }



    }



}
