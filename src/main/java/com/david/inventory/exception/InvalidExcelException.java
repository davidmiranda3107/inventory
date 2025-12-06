package com.david.inventory.exception;

import java.util.List;

public class InvalidExcelException extends RuntimeException {
    private final List<String> errors;

    public InvalidExcelException(List<String> errors) {
        super("Excel import contains errors");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
