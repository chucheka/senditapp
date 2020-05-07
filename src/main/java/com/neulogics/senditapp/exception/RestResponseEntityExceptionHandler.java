package com.neulogics.senditapp.exception;

import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;



@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({AccessDeniedException.class})
	public ResponseEntity<ErrorResponse> handleAccessDeniedException(Exception ex,WebRequest req){
		ErrorResponse error = new ErrorResponse();
				error.setStatus(HttpStatus.FORBIDDEN.value());
				error.setMessage(ex.getMessage());
				error.setTimeStamp(System.currentTimeMillis());
				return new ResponseEntity<ErrorResponse>(error,HttpStatus.FORBIDDEN);	
						
	}
}

