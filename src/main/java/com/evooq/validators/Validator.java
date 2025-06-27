package com.evooq.validators;

import java.util.Optional;

public interface Validator {
    Optional<Error> validate(String[] args);
}
