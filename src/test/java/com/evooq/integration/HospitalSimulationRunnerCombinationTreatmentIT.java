package com.evooq.integration;

import com.evooq.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(args = {"T,F,D", "An,I"})
class HospitalSimulationRunnerCombinationTreatmentIT {

    @Autowired
    PatientService patientService;

    @Test
    void testCombinationTreatment() {
        var patients = patientService.findAll();

        long feverCount = patients.stream().filter(p -> p.getState().name().equals("FEVER")).count();
        long healthyCount = patients.stream().filter(p -> p.getState().name().equals("HEALTHY")).count();
        long diabetesCount = patients.stream().filter(p -> p.getState().name().equals("DIABETES")).count();
        long tuberculosisCount = patients.stream().filter(p -> p.getState().name().equals("TUBERCULOSIS")).count();
        long deadCount = patients.stream().filter(p -> p.getState().name().equals("DEAD")).count();

        assertEquals(1, feverCount);
        assertEquals(1, healthyCount);
        assertEquals(0, diabetesCount);
        assertEquals(0, tuberculosisCount);
        assertEquals(1, deadCount);
    }
}