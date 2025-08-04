package com.webstore.userservice.handler;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> errors
) {
}
