package com.evooq.service;

import com.evooq.model.Drug;
import com.evooq.model.Gender;
import com.evooq.model.Patient;
import com.evooq.model.PatientState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PatientAssessmentServiceTest {

    PatientService patientService;
    PatientAssessmentService assessmentService;
    Supplier<Double> randomSupplier;

    @BeforeEach
    void setUp() {
        patientService = mock(PatientService.class);
        randomSupplier = mock(Supplier.class);
        assessmentService = new PatientAssessmentService(patientService, randomSupplier);
    }

    @ParameterizedTest
    @EnumSource(value = Drug.class, names = {"PARACETAMOL", "ASPIRIN"})
    void testCheckOnFeverPatientWithRequiredDrugs(Drug drug) {
        var patient = createPatient(PatientState.FEVER, List.of(drug));
        assessmentService.checkOnPatient(patient);

        assertEquals(PatientState.HEALTHY, patient.getState());
        verify(patientService).save(patient);
    }

    @Test
    void testCheckOnFeverPatientWithoutDrugs() {
        var patient = createPatient(PatientState.FEVER, List.of());
        assessmentService.checkOnPatient(patient);

        assertEquals(PatientState.FEVER, patient.getState());
        verify(patientService).save(patient);
    }

    @ParameterizedTest
    @EnumSource(value = Drug.class, names = {"INSULIN", "ANTIBIOTIC"})
    void testCheckOnFeverPatientWithDrugsThatHaveNoEffect(Drug drug) {
        var patient = createPatient(PatientState.FEVER, List.of(drug));
        assessmentService.checkOnPatient(patient);

        assertEquals(PatientState.FEVER, patient.getState());
        verify(patientService).save(patient);
    }

    @Test
    void testCheckOnTuberculosisPatientWithAntibiotic() {
        var patient = createPatient(PatientState.TUBERCULOSIS, List.of(Drug.ANTIBIOTIC));
        assessmentService.checkOnPatient(patient);

        assertEquals(PatientState.HEALTHY, patient.getState());
        verify(patientService).save(patient);
    }

    @Test
    void testCheckOnTuberculosisPatientWithoutAntibiotic() {
        var patient = createPatient(PatientState.TUBERCULOSIS, List.of());
        assessmentService.checkOnPatient(patient);

        assertEquals(PatientState.TUBERCULOSIS, patient.getState());
        verify(patientService).save(patient);
    }

    @ParameterizedTest
    @EnumSource(value = Drug.class, names = {"PARACETAMOL", "ASPIRIN", "INSULIN"})
    void testCheckOnTuberculosisPatientWithOtherDrugs(Drug drug) {
        var patient = createPatient(PatientState.TUBERCULOSIS, List.of(drug));
        assessmentService.checkOnPatient(patient);

        assertEquals(PatientState.TUBERCULOSIS, patient.getState());
        verify(patientService).save(patient);
    }

    @Test
    void testCheckOnDiabetesPatientWithInsulin() {
        var patient = createPatient(PatientState.DIABETES, List.of(Drug.INSULIN));
        assessmentService.checkOnPatient(patient);

        assertEquals(PatientState.DIABETES, patient.getState());
        verify(patientService).save(patient);
    }

    @Test
    void testCheckOnDiabetesPatientWithoutInsulin() {
        var patient = createPatient(PatientState.DIABETES, List.of());
        assessmentService.checkOnPatient(patient);

        assertEquals(PatientState.DEAD, patient.getState());
        verify(patientService).save(patient);
    }

    @ParameterizedTest
    @EnumSource(value = Drug.class, names = {"PARACETAMOL", "ASPIRIN", "ANTIBIOTIC"})
    void testCheckOnDiabetesPatientWithOtherDrugs(Drug drug) {
        var patient = createPatient(PatientState.DIABETES, List.of(drug));
        assessmentService.checkOnPatient(patient);

        assertEquals(PatientState.DEAD, patient.getState());
        verify(patientService).save(patient);
    }

    @Test
    void testCheckOnHealthyPatientWithInsulinAndAntibiotic() {
        var patient = createPatient(PatientState.HEALTHY, List.of(Drug.INSULIN, Drug.ANTIBIOTIC));
        assessmentService.checkOnPatient(patient);
        assertEquals(PatientState.FEVER, patient.getState());
        verify(patientService).save(patient);
    }

    @Test
    void testCheckOnHealthyPatientWithoutDrugs() {
        var patient = createPatient(PatientState.HEALTHY, List.of());
        assessmentService.checkOnPatient(patient);

        assertEquals(PatientState.HEALTHY, patient.getState());
        verify(patientService).save(patient);
    }

    @ParameterizedTest
    @EnumSource(value = Drug.class)
    void testCheckOnHealthyPatientWithOtherDrugs(Drug drug) {
        var patient = createPatient(PatientState.HEALTHY, List.of(drug));
        assessmentService.checkOnPatient(patient);

        assertEquals(PatientState.HEALTHY, patient.getState());
        verify(patientService).save(patient);
    }

    @Test
    void testCheckOnDeadPatientOneInAMillionChance() {
        var patient = createPatient(PatientState.DEAD, List.of());
        when(randomSupplier.get()).thenReturn(0.0);

        assessmentService.checkOnPatient(patient);

        assertEquals(PatientState.HEALTHY, patient.getState());
        verify(patientService).save(patient);
    }

    @Test
    void testCheckOnDeadPatientNoChance() {
        var patient = createPatient(PatientState.DEAD, List.of());
        when(randomSupplier.get()).thenReturn(0.999999);

        assessmentService.checkOnPatient(patient);

        assertEquals(PatientState.DEAD, patient.getState());
        verify(patientService).save(patient);
    }

    private Patient createPatient(PatientState state, List<Drug> drugs) {
        Patient patient = new Patient();
        patient.setName("Test");
        patient.setAge(30);
        patient.setGender(Gender.FEMALE);
        patient.setState(state);
        patient.setDrugList(drugs);
        return patient;
    }
}