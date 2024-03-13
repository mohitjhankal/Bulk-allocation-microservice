package com.newbury.revola.errors;

import com.newbury.revola.domain.ErrorDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.UUID;

@RestControllerAdvice
public class ExceptionTranslator extends ResponseEntityExceptionHandler {

	// Error Data card
	@Value("${dmm.general.error.topic}")
	String generalErrorTopic;

	@Value("${dmm.general.error.resolution.status}")
	String errorDetailResolutionStatus;

	@Value("${dmm.general.error.service.name}")
	String errorDetailServiceName;

	@Autowired
	private KafkaTemplate<String, ErrorDetails> kafkaTemplate;

	@ExceptionHandler(BadRequestAlertException.class)
	public ResponseEntity<CustomErrorResponse> customHandleNotFound(BadRequestAlertException ex) {
		CustomErrorResponse errors = new CustomErrorResponse();
		errors.setTimestamp(LocalDateTime.now());
		errors.setStatusCode(HttpStatus.BAD_REQUEST.value());
		errors.setStatusMessage("Failure");
		errors.setErrorDescription(ex.getMessage());
		errors.setPath(ex.getPath());
		ErrorDetails errorDetails = new ErrorDetails();
		errorDetails.setTimeStamp(LocalDateTime.now());
		errorDetails.setResolutionStatus(errorDetailResolutionStatus);
		errorDetails.setService(errorDetailServiceName);
		errorDetails.setErrorDescription(ex.getMessage());
		errorDetails.setPath(ex.getPath());
		kafkaTemplate.send(generalErrorTopic, UUID.randomUUID().toString(), errorDetails);
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<CustomErrorResponse> resourceHandleNotFound(ResourceNotFoundException ex) {
		CustomErrorResponse errors = new CustomErrorResponse();
		errors.setTimestamp(LocalDateTime.now());
		errors.setStatusCode(HttpStatus.NOT_FOUND.value());
		errors.setStatusMessage("Failure");
		errors.setErrorDescription(ex.getMessage());
		errors.setPath(ex.getPath());
		ErrorDetails errorDetails = new ErrorDetails();
		errorDetails.setTimeStamp(LocalDateTime.now());
		errorDetails.setResolutionStatus(errorDetailResolutionStatus);
		errorDetails.setService(errorDetailServiceName);
		errorDetails.setErrorDescription(ex.getMessage());
		errorDetails.setPath(ex.getPath());
		kafkaTemplate.send(generalErrorTopic, UUID.randomUUID().toString(), errorDetails);
		return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
		CustomErrorResponse errors = new CustomErrorResponse();
		errors.setTimestamp(LocalDateTime.now());
		errors.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errors.setStatusMessage("Internal Server Error");
		errors.setErrorDescription(ex.getMessage());
		ErrorDetails errorDetails = new ErrorDetails();
		errorDetails.setTimeStamp(LocalDateTime.now());
		errorDetails.setResolutionStatus(errorDetailResolutionStatus);
		errorDetails.setService(errorDetailServiceName);
		errorDetails.setErrorDescription(ex.getMessage());
		kafkaTemplate.send(generalErrorTopic, UUID.randomUUID().toString(), errorDetails);
		return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);

	}

}