package com.junpak.cashbook.presentation.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
	public ResponseEntity<ExceptionResponse> handleBusinessException(RuntimeException exception) {
		return ResponseEntity.badRequest()
			.body(new ExceptionResponse(exception.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> handleValidationException() {
		return ResponseEntity.badRequest()
			.body(new ExceptionResponse("요청 값이 올바르지 않습니다."));
	}
}
