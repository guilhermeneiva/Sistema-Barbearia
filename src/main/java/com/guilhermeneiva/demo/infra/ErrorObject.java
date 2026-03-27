package com.guilhermeneiva.demo.infra;

public record ErrorObject(
        String message,
        String field,
        Object parameter
) {
}
