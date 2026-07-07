package com.sanfernando.erpclinica.exception;

public record FieldValidationError(
        String field,
        String message
) {
}