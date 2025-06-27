package com.evooq.validators;

import java.util.List;

public class ValidationException extends RuntimeException {
    public ValidationException(List<String> errorList) {
        super(String.join(", ", errorList));
    }
}
