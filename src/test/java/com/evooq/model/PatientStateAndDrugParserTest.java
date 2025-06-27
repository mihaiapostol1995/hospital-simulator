package com.evooq.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PatientStateAndDrugParserTest {

    @Test
    void testInvalidPatientStateToken() {
        var invalidToken = "Y";
        var exception = assertThrows(IllegalArgumentException.class, () -> PatientState.fromToken(invalidToken));
        assertEquals("Invalid token: " + invalidToken, exception.getMessage());
    }

    @Test
    void testInvalidDrugToken() {
        var invalidToken = "Y";
        var exception = assertThrows(IllegalArgumentException.class, () -> Drug.fromToken(invalidToken));
        assertEquals("Invalid token: " + invalidToken, exception.getMessage());
    }
}