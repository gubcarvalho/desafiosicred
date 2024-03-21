package com.nt.desafiosicred.config;

import com.nt.desafiosicred.exceptions.ResourceNotFoundException;
import com.nt.desafiosicred.exceptions.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	public static final String ERROR_ALIAS = "error";

	private final MessageSource messageSource;

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleGenException(final Exception ex, final WebRequest request) {
		return logException(ex, HttpStatus.INTERNAL_SERVER_ERROR, request, true);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> handleResourceNotFoundException(final ResourceNotFoundException ex, final WebRequest request) {
		return logException(ex, HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<Object> handleValidationException(final ValidationException ex, final WebRequest request) {
		return logException(ex, HttpStatus.UNPROCESSABLE_ENTITY, request);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<Object> handleBadRequestConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
		return logException(ex, HttpStatus.BAD_REQUEST, request,
			Optional.ofNullable(ex)
				.map(ConstraintViolationException::getConstraintViolations)
				.orElse(Collections.emptySet())
				.stream()
				.filter(cns -> Objects.nonNull(cns.getPropertyPath()))
				.map(this::createConstraintError)
				.toList()
		);
	}

	private LocalFieldError createConstraintError(ConstraintViolation<?> constraintViolation) {
		return new LocalFieldError(StringUtils.capitalize(constraintViolation.getPropertyPath().toString()), constraintViolation.getMessage());
	}

	private ResponseEntity<Object> logException(final Exception ex, final HttpStatus httpStatus, final WebRequest request) {
        return logException(ex, httpStatus, request, Collections.emptyList());
	}

	private ResponseEntity<Object> logException(final Exception ex, final HttpStatus httpStatus, final WebRequest request, boolean showTrace) {
        return logException(ex, httpStatus, request, Collections.emptyList(), showTrace);
	}
	
	private ResponseEntity<Object> logException(final Exception ex, final HttpStatus httpStatus, final WebRequest request, List<LocalFieldError> errors) {
		return logException(ex, httpStatus, request, errors, false);
	}

	private ResponseEntity<Object> logException(final Exception ex, final HttpStatus httpStatus, final WebRequest request, List<LocalFieldError> errors, boolean showTrace) {
		String message = ex.getMessage();
		if (showTrace) {
			logger.error(String.format("Request: %s, error: %s", request.toString(), message), ex);
		} else {
			logger.error(String.format("Request: %s, error: %s", request.toString(), message));
		}
		final Map<String, Object> mapErrors = Map.of(ERROR_ALIAS, !errors.isEmpty() ? errors : message);
		return ResponseEntity.status(httpStatus).body(mapErrors);
	}

	@Getter
	@RequiredArgsConstructor
	private class LocalFieldError {

		private final String name;
		private final String error;
	}
}
