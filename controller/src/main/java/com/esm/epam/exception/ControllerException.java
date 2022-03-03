package com.esm.epam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ControllerException extends RuntimeException {
    private String message;
}
