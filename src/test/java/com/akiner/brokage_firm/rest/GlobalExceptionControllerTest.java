package com.akiner.brokage_firm.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class GlobalExceptionControllerTest {

    private GlobalExceptionController globalExceptionController;

    @BeforeEach
    public void setUp() {
        globalExceptionController = new GlobalExceptionController();
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @DisplayName("Handle exception with message")
    @Test
    public void handleException_withMessage() {
        Exception exception = new Exception("Test exception");
        ErrorResponse errorResponse = globalExceptionController.handleException(exception);

        assertEquals("Test exception", errorResponse.getMessage());
    }

    @DisplayName("Handle exception without message")
    @Test
    public void handleException_withoutMessage() {
        Exception exception = Mockito.mock(Exception.class);
        Throwable throwable = Mockito.mock(Throwable.class);
        when(exception.getMessage()).thenReturn(null);
        when(exception.getCause()).thenReturn(throwable);
        when(throwable.toString()).thenReturn("java.lang.Exception");
        ErrorResponse errorResponse = globalExceptionController.handleException(exception);

        assertEquals("java.lang.Exception", errorResponse.getMessage());
    }
}
