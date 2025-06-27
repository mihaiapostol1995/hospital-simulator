package com.evooq.validators;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationChain {

    private final List<Validator> validators = new ArrayList<>();

    public static ValidationChain build() {
        return new ValidationChain();
    }

    public ValidationChain add(Validator validator) {
        validators.add(validator);
        return this;
    }

    public void validate(String[] args) {
        var errorList = validators.stream()
                .map(v -> v.validate(args))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Error::message)
                .toList();
        if (!errorList.isEmpty()) {
            throw new ValidationException(errorList);
        }
    }
}