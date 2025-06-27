package com.evooq.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum Drug {
    ASPIRIN("Aa"),
    ANTIBIOTIC("An"),
    INSULIN("I"),
    PARACETAMOL("P");

    private final String token;

    public static Set<String> drugs() {
        return Arrays.stream(Drug.values()).map(s -> s.token).collect(Collectors.toSet());
    }

    public static Drug fromToken(String token) {
        return Arrays.stream(Drug.values())
                .filter(drug -> drug.token.equals(token))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid token: " + token));
    }
}
