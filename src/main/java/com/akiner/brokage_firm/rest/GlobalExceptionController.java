package com.akiner.brokage_firm.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RestControllerAdvice
public class GlobalExceptionController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = Exception.class)
    public @ResponseBody ErrorResponse handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse();

        if (e.getMessage() == null) {
            errorResponse.setMessage(e.getCause().toString());
        } else {
            errorResponse.setMessage(e.getMessage());
        }
        return errorResponse;
    }
}
