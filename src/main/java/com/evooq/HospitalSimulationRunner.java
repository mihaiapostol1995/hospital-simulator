package com.evooq;

import com.evooq.service.DrugService;
import com.evooq.service.PatientAssessmentService;
import com.evooq.service.PatientService;
import com.evooq.util.PatientPrinter;
import com.evooq.validators.ArgumentsValidator;
import com.evooq.validators.Error;
import com.evooq.validators.ValidationChain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.IntStream;

import static com.evooq.model.Drug.drugs;
import static com.evooq.model.PatientState.patientStates;

@Component
@Slf4j
@RequiredArgsConstructor
public class HospitalSimulationRunner implements CommandLineRunner {

    private static final int MAXIMUM_ALLOWED_PARAMETERS_COUNT = 2;

    private final PatientService patientService;
    private final DrugService drugService;
    private final PatientAssessmentService patientAssessmentService;
    private final PatientPrinter patientPrinter;

    @Override
    public void run(String... args) {
        IntStream.range(0, args.length)
                .forEach(i -> log.info("Argument {}: {}", i, args[i]));

        ValidationChain.build()
                .add(arg -> args.length == 0
                        ? Optional.of(new Error("Missing required arguments"))
                        : Optional.empty())
                .add(arg -> args.length > MAXIMUM_ALLOWED_PARAMETERS_COUNT
                        ? Optional.of(new Error("Invalid number of arguments. Expected at most %d, got %d"
                            .formatted(MAXIMUM_ALLOWED_PARAMETERS_COUNT, args.length)))
                        : Optional.empty())
                .add(new ArgumentsValidator(patientStates(), 0))
                .add(new ArgumentsValidator(drugs(), 1))
                .validate(args);

        patientService.createPatients(args[0]);
        if (args.length == MAXIMUM_ALLOWED_PARAMETERS_COUNT) {
            drugService.assignDrugsToPatients(args[1]);
        }

        patientService.findAll()
                .forEach(patientAssessmentService::checkOnPatient);
        patientPrinter.printPatientSummary();
    }
}

