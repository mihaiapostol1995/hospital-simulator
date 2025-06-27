package com.evooq.validators;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class ArgumentsValidator implements Validator {

    private final Set<String> validStates;
    private final int parameterIndex;

    @Override
    public Optional<Error> validate(String[] args) {
        if (args.length <= parameterIndex) {
            return Optional.empty();
        }

        var patientStates = args[parameterIndex];
        var invalidStates = Arrays.stream(patientStates.split(","))
                .filter(parameter -> !validStates.contains(parameter))
                .toList();

        return invalidStates.isEmpty()
                ? Optional.empty()
                : Optional.of(new Error("Invalid states: %s. Possible valid states are: %s"
                .formatted(invalidStates, validStates)));
    }
}