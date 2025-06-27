package com.evooq.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum PatientState {
    FEVER("F"),
    HEALTHY("H"),
    DIABETES("D"),
    TUBERCULOSIS("T"),
    DEAD("X");

    private final String token;

    public static Set<String> patientStates() {
        return Arrays.stream(PatientState.values()).map(s -> s.token).collect(Collectors.toSet());
    }

    public static PatientState fromToken(String token) {
        return Arrays.stream(PatientState.values())
                .filter(ps -> ps.token.equals(token))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid token: " + token));
    }
}
