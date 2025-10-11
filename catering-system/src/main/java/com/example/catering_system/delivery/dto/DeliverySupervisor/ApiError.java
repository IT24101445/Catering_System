package com.example.catering_system.delivery.dto.DeliverySupervisor;


import java.time.Instant;

public class ApiError {
    private String message;
    private Instant timestamp = Instant.now();

    public ApiError() {
    }

    public ApiError(String message) {
        this.message = message;
        this.timestamp = Instant.now();
    }

    public static ApiError of(String message) {
        return new ApiError(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}