package com.empik.empikinterviewtask.exceptions;

import java.time.LocalDateTime;

public record RestApiError(int code, String message, LocalDateTime timestamp) {
}
