package com.trinhquycong.restloginreviewcenter.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GeneralExceptionsHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(value = {ResponseStatusException.class})
	protected ResponseEntity<Object> handleAccessDenied(ResponseStatusException e, WebRequest request) {
        return handleExceptionInternal(e, e.getReason(), 
          new HttpHeaders(), e.getStatus(), request);
	}

}
