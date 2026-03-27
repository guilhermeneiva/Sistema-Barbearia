package com.guilhermeneiva.demo.infra;

import java.util.List;

public record ErrorResponse(
        String message,
        int code,
        String status,
        String objectName,
        List<ErrorObject> errors
) {
}
