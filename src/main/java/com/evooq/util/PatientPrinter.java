package com.evooq.util;

import com.evooq.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class PatientPrinter {

    private final PatientService patientService;

    public void printPatientSummary() {
        var stateCounts = patientService.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        patient -> patient.getState().getToken(),
                        Collectors.counting()
                ));

        var summary = String.format(
                "F:%d,H:%d,D:%d,T:%d,X:%d",
                stateCounts.getOrDefault("F", 0L),
                stateCounts.getOrDefault("H", 0L),
                stateCounts.getOrDefault("D", 0L),
                stateCounts.getOrDefault("T", 0L),
                stateCounts.getOrDefault("X", 0L)
        );
        log.info(summary);
    }
}
