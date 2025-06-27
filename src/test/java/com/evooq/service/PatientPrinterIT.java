package com.evooq.service;

import com.evooq.util.PatientPrinter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(args = {"D,D"})
@ExtendWith(OutputCaptureExtension.class)
class PatientPrinterIT {

    @Autowired
    PatientPrinter patientPrinter;

    @Test
    void testPrintPatientSummary(CapturedOutput output) {
        patientPrinter.printPatientSummary();

        var printedOutput = output.toString();
        assertThat(printedOutput).contains("F:0,H:0,D:0,T:0,X:2");
    }
}