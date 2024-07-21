package com.chat.app.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalException {

	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorDetail> UserExceptionHandler(UserException e, WebRequest req) {
		ErrorDetail err = new ErrorDetail(e.getMessage(), req.getDescription(false), LocalDateTime.now());
		
		return new ResponseEntity<ErrorDetail>(err, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MessageException.class)
	public ResponseEntity<ErrorDetail> MessageExceptionHandler(MessageException ue, WebRequest req) {
		ErrorDetail err = new ErrorDetail(ue.getMessage(), req.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<ErrorDetail>(err, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ChannelException.class)
	public ResponseEntity<ErrorDetail> ChatExceptionHandler(ChannelException ue, WebRequest req) {
		ErrorDetail err = new ErrorDetail(ue.getMessage(), req.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<ErrorDetail>(err, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDetail> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException me) {
		String error = me.getBindingResult().getFieldError().getDefaultMessage();
		ErrorDetail err = new ErrorDetail("Validation Error", error, LocalDateTime.now());
		return new ResponseEntity<ErrorDetail>(err, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ErrorDetail> handleNoHandlerNotFoundException(NoHandlerFoundException ex, WebRequest req) {
		ErrorDetail error = new ErrorDetail("Endpoint not found", ex.getMessage(), LocalDateTime.now());
		return new ResponseEntity<ErrorDetail>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetail> OtherExceptionHandler(Exception e, WebRequest req) {
		ErrorDetail err = new ErrorDetail(e.getMessage(), req.getDescription(false), LocalDateTime.now());
		
		return new ResponseEntity<ErrorDetail>(err, HttpStatus.BAD_REQUEST);
	}
}
