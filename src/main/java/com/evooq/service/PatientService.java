package com.evooq.service;

import com.evooq.model.Gender;
import com.evooq.model.Patient;
import com.evooq.model.PatientState;
import com.evooq.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.evooq.util.NameGenerator.AVAILABLE_NAMES;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {

    private final PatientRepository patientRepository;

    public void saveAll(List<Patient> patients) {
        patientRepository.saveAll(patients);
    }

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    public void createPatients(String patientStates) {
        var patients = Arrays.stream(patientStates.split(","))
                .map(PatientState::fromToken)
                .map(PatientService::createPatient)
                .toList();
        log.debug("Saving patients into database: {}", patients);
        saveAll(patients);
    }

    private static Patient createPatient(PatientState state) {
        var random = new Random();

        var patient = new Patient();
        patient.setName(AVAILABLE_NAMES.get(random.nextInt(AVAILABLE_NAMES.size())));

        var age = 18 + random.nextInt(50);
        patient.setAge(age);

        patient.setGender(Gender.values()[random.nextInt(Gender.values().length)]);
        patient.setState(state);
        return patient;
    }
}
