package com.evooq.service;

import com.evooq.model.Drug;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class DrugService {

    public final PatientService patientService;

    public void assignDrugsToPatients(String drugs) {
        var drugList = Arrays.stream(drugs.split(","))
                .map(Drug::fromToken)
                .toList();

        var allPatients = patientService.findAll();
        for (var i = 0; i < drugList.size(); i++) {
            var patient = allPatients.get(i);
            var drug = drugList.get(i);
            log.debug("Administering drug {} to patient {} suffering from {}", drug, patient.getName(),
                    patient.getState());
            patient.administerDrug(drug);
        }

        patientService.saveAll(allPatients);
    }
}
