package com.hotel.management.exception;

import java.sql.SQLIntegrityConstraintViolationException;

import javax.validation.ValidationException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hotel.management.response.AppResponse;

import io.jsonwebtoken.SignatureException;

@ControllerAdvice
public class ExceptionHandlingController {

	@ExceptionHandler(AppException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public AppResponse<ErrorResponse> resourceNotFound(AppException ex) {
		ErrorResponse response = new ErrorResponse();

		if (ex.getResponseDetail() == null) {
			response.setStatus(400);
			response.setMessage(ex.getCustomMessage());
			response.setUiErrorKey("error_resource_exists_error");
			return new AppResponse<>(HttpStatus.BAD_REQUEST, 400, response);
		}
		response.setStatus(ex.getResponseDetail().getCode());
		response.setMessage(ex.getResponseDetail().getMessage());
		response.setUiErrorKey(ex.getResponseDetail().getUiErrorKey());

		return new AppResponse<>(HttpStatus.BAD_REQUEST, 400, response);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	// public Map<String, String>
	// handleValidationExceptions(MethodArgumentNotValidException ex) {
	public AppResponse<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

		// Map<String, String> errors = new HashMap<>();

		ErrorResponse response = new ErrorResponse();

		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();

			response.setStatus(400);
			response.setMessage(fieldName + " - " + errorMessage);
			response.setUiErrorKey("error_validation_failure");

			// errors.put(fieldName, errorMessage);
		});

		return new AppResponse<>(HttpStatus.BAD_REQUEST, 400, response);
	}

	@ExceptionHandler(InternalError.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public AppResponse<ErrorResponse> internalException(AppException ex) {
		ErrorResponse response = new ErrorResponse();
		response.setStatus(ex.getResponseDetail().getCode());
		response.setMessage(ex.getResponseDetail().getMessage());
		response.setUiErrorKey(ex.getResponseDetail().getUiErrorKey());

		return new AppResponse<>(HttpStatus.BAD_REQUEST, 400, response);
	}

	// MissingServletRequestParameterException for handle request paramater missing
	// exception
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public AppResponse<ErrorResponse> missingServletRequestException(MissingServletRequestParameterException ex) {
		ErrorResponse response = new ErrorResponse();
		response.setStatus(400);
		response.setMessage("Parameter missing exception");
		response.setUiErrorKey("error_validation_failure");
		return new AppResponse<>(HttpStatus.BAD_REQUEST, 400, response);
	}

	// HttpMessageNotReadable request missing Exception handled
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public AppResponse<ErrorResponse> missingRequestBodyException(HttpMessageNotReadableException ex) {

		ErrorResponse response = new ErrorResponse();
		response.setStatus(400);
		response.setMessage("Request missing exception");
		response.setUiErrorKey("request_missing_error");
		return new AppResponse<>(HttpStatus.BAD_REQUEST, 400, response);
	}

	// ValidationException the validation error handled
	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public AppResponse<ErrorResponse> emptyRequestException(ValidationException ex) {
		ErrorResponse response = new ErrorResponse();
		response.setStatus(400);
		response.setMessage("Input validation exception");
		response.setUiErrorKey("validation_error");
		return new AppResponse<>(HttpStatus.BAD_REQUEST, 400, response);
	}

//	// Entity not null exception handled
//	@ExceptionHandler(InvalidDataAccessApiUsageException.class)
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	@ResponseBody
//	public AppResponse<ErrorResponse> entityNotNullException(InvalidDataAccessApiUsageException ex) {
//		ErrorResponse response = new ErrorResponse();
//		response.setStatus(404);
//		response.setMessage("Entity must not be null");
//		response.setUiErrorKey("entity_null_exception");
//		return new AppResponse<>(HttpStatus.NOT_FOUND, 404, response);
//	}

	// SQL Integrity Exception DB Error handled
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public AppResponse<ErrorResponse> foreignKeyConstraintException(SQLIntegrityConstraintViolationException ex) {
		ErrorResponse response = new ErrorResponse();
		response.setStatus(400);
		response.setMessage("Cannot update or delete the entity");
		response.setUiErrorKey("integrity_constraint_exception");
		return new AppResponse<>(HttpStatus.BAD_REQUEST, 400, response);
	}

	// Handled the EmptyResult Exception
	@ExceptionHandler(EmptyResultDataAccessException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public AppResponse<ErrorResponse> emptyDataAccessException(EmptyResultDataAccessException ex) {
		ErrorResponse response = new ErrorResponse();
		response.setStatus(404);
		response.setMessage("Requested value not here");
		response.setUiErrorKey("no_entity_presents");
		return new AppResponse<>(HttpStatus.NOT_FOUND, 404, response);
	}

	// Handled the json webtoken Exception
	@ExceptionHandler(SignatureException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public AppResponse<ErrorResponse> jsonWebTokenException(SignatureException ex) {
		ErrorResponse response = new ErrorResponse();
		response.setStatus(400);
		response.setMessage("jwt token does not match");
		response.setUiErrorKey("jwt_token_exception");
		return new AppResponse<>(HttpStatus.NOT_FOUND, 404, response);
	}
}