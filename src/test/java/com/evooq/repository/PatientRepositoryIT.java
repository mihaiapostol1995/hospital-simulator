package com.evooq.repository;

import com.evooq.model.Patient;
import com.evooq.model.PatientState;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.transaction.TestTransaction;

import static com.evooq.model.Drug.ASPIRIN;
import static com.evooq.model.Drug.INSULIN;
import static com.evooq.model.Drug.PARACETAMOL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(args = "F")
class PatientRepositoryIT {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void setUp() {
        patientRepository.deleteAll();
    }

    @Test
    void testSaveAndUpdatePatient() {
        var patient = createNewPatient();

        var savedPatient = patientRepository.save(patient);

        assertNotNull(savedPatient.getId());
        assertEquals("John Doe", savedPatient.getName());
        assertEquals(PatientState.FEVER, savedPatient.getState());
        assertEquals(1, savedPatient.getDrugList().size());
        assertEquals("ASPIRIN", savedPatient.getDrugList().getFirst().name());
        assertEquals("system", savedPatient.getCreatedBy());
        assertNotNull(savedPatient.getCreatedDate());
        assertEquals("system", savedPatient.getLastModifiedBy());
        assertNotNull(savedPatient.getLastModifiedDate());
        assertEquals(0, savedPatient.getVersion());

        savedPatient.getDrugList().clear();
        savedPatient.administerDrug(PARACETAMOL);
        savedPatient.administerDrug(INSULIN);
        var updatedPatient = patientRepository.save(savedPatient);

        assertEquals(2, updatedPatient.getDrugList().size());
        assertEquals("PARACETAMOL", updatedPatient.getDrugList().getFirst().name());
        assertEquals("INSULIN", updatedPatient.getDrugList().get(1).name());
        assertEquals(1, updatedPatient.getVersion(), "Version should be updated after modification");
    }

    @Test
    @Transactional
    void testEnversAuditEntryCreated() {
        var patient = createNewPatient();

        var savedPatient = patientRepository.save(patient);
        commitTransaction();

        assertNotNull(savedPatient.getId());

        var auditReader = AuditReaderFactory.get(entityManager);
        var revisions = auditReader.getRevisions(Patient.class, savedPatient.getId());

        assertNotNull(revisions);
        assertEquals(1, revisions.size());

        var auditedPatient = auditReader.find(Patient.class, savedPatient.getId(), revisions.getFirst());
        assertEquals("John Doe", auditedPatient.getName());
        assertEquals(PatientState.FEVER, auditedPatient.getState());
        assertEquals(1, auditedPatient.getDrugList().size());
        assertEquals("ASPIRIN", auditedPatient.getDrugList().getFirst().name());
    }

    private static Patient createNewPatient() {
        var patient = new Patient();
        patient.setName("John Doe");
        patient.setState(PatientState.FEVER);
        patient.administerDrug(ASPIRIN);
        patient.setAge(29);
        return patient;
    }

    // used so that the envers entity is persisted, like a flush in the transaction
    private void commitTransaction() {
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();
    }
}