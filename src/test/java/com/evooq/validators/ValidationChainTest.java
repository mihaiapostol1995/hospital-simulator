package com.evooq.validators;

import com.evooq.HospitalSimulationRunner;
import com.evooq.service.DrugService;
import com.evooq.service.PatientAssessmentService;
import com.evooq.service.PatientService;
import com.evooq.util.PatientPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ValidationChainTest {

    PatientService patientService;
    DrugService drugService;
    PatientAssessmentService patientAssessmentService;
    PatientPrinter patientPrinter;
    HospitalSimulationRunner runner;

    @BeforeEach
    void setUp() {
        patientService = mock(PatientService.class);
        drugService = mock(DrugService.class);
        patientAssessmentService = mock(PatientAssessmentService.class);
        patientPrinter = mock(PatientPrinter.class);
        runner = new HospitalSimulationRunner(patientService, drugService, patientAssessmentService, patientPrinter);
    }

    @Test
    void testRunWithNoArguments() {
        var validationException = assertThrows(ValidationException.class, () -> runner.run());
        assertEquals("Missing required arguments", validationException.getMessage());
    }

    @Test
    void testErrorsAreAccumulated() {
        var validationException = assertThrows(ValidationException.class, () -> runner.run("F", "H", "D"));
        assertEquals("Invalid number of arguments. Expected at most 2, got 3, Invalid states: [H]. Possible valid states are: [Aa, P, I, An]",
                validationException.getMessage());
    }

    @Test
    void testInvalidPatientState() {
        var validationException = assertThrows(ValidationException.class, () -> runner.run("F,O"));
        assertEquals("Invalid states: [O]. Possible valid states are: [D, T, F, H, X]", validationException.getMessage());
    }

    @Test
    void testInvalidDrugState() {
        var validationException = assertThrows(ValidationException.class, () -> runner.run("F", "1"));
        assertEquals("Invalid states: [1]. Possible valid states are: [Aa, P, I, An]",
                validationException.getMessage());
    }

    @Test
    void testRunWithValidArguments() {
        runner.run("F", "Aa");
        verify(patientService).createPatients("F");
        verify(drugService).assignDrugsToPatients("Aa");
        verify(patientPrinter).printPatientSummary();
    }
}