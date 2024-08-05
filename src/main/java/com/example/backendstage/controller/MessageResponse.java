package com.example.backendstage.controller;

import lombok.Data;
import lombok.NonNull;

@Data
public class MessageResponse {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
