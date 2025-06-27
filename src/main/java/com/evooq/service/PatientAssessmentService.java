package com.evooq.service;

import com.evooq.model.Drug;
import com.evooq.model.Patient;
import com.evooq.model.PatientState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.function.Supplier;

@Service
@Slf4j
@RequiredArgsConstructor
public class PatientAssessmentService {

    private final PatientService patientService;
    private final Supplier<Double> randomSupplier;

    @SuppressWarnings("checkstyle:MissingSwitchDefault")
    public void checkOnPatient(Patient patient) {
        switch (patient.getState()) {
            case FEVER -> checkFeverPatient(patient);
            case TUBERCULOSIS -> checkTuberculosisPatient(patient);
            case DIABETES -> checkDiabetesPatient(patient);
            case HEALTHY -> checkHealthyPatient(patient);
            case DEAD -> checkDeadPatient(patient);
        }

        patientService.save(patient);
    }

    private void checkDeadPatient(Patient patient) {
        var oneInAMillion = randomSupplier.get() < 1.0 / 1_000_000;
        if (oneInAMillion) {
            log.info("One in a million chance for dead patient {}", patient.getName());
            patient.setState(PatientState.HEALTHY);
        }
    }

    private void checkHealthyPatient(Patient patient) {
        var drugList = patient.getDrugList();
        if (drugList.contains(Drug.INSULIN) && drugList.contains(Drug.ANTIBIOTIC)) {
            log.info("Healthy patient {} is getting a fever", patient.getName());
            patient.setState(PatientState.FEVER);
        }
    }

    private void checkDiabetesPatient(Patient patient) {
        patient.getDrugList()
                .stream()
                .filter(drug -> drug == Drug.INSULIN)
                .findAny()
                .ifPresentOrElse(p -> log.info("Diabetes patient {} stays alive", patient.getName()),
                        () -> {
                            patient.setState(PatientState.DEAD);
                            log.info("Diabetes patient {} has died because he hasn't received insulin", patient.getName());
                        });
    }

    private void checkTuberculosisPatient(Patient patient) {
        patient.getDrugList()
                .stream()
                .filter(drug -> drug == Drug.ANTIBIOTIC)
                .findAny()
                .ifPresentOrElse(p -> {
                    log.info("Tuberculosis patient {} has been cured", patient.getName());
                    patient.setState(PatientState.HEALTHY);
                }, () -> log.info("Patient {} has not been cured", patient.getName()));
    }

    private void checkFeverPatient(Patient patient) {
        patient.getDrugList()
                .stream()
                .filter(drug -> EnumSet.of(Drug.PARACETAMOL, Drug.ASPIRIN).contains(drug))
                .findAny()
                .ifPresentOrElse(p -> {
                    log.info("Fever patient {} has been cured", patient.getName());
                    patient.setState(PatientState.HEALTHY);
                }, () -> log.info("Patient {} has not been cured", patient.getName()));
    }
}
