package com.ddos.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExceptionBody {
    private String message;
    private List<String> errors;


    public ExceptionBody(String message) {
        this.message = message;
    }
}
