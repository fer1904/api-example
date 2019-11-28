package com.api.exercise.handler;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@SuppressWarnings("unused")
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus
      status, WebRequest request) {
    return super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status,
                                                                   WebRequest request) {
    return super.handleHttpMediaTypeNotSupported(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status,
                                                                    WebRequest request) {
    return super.handleHttpMediaTypeNotAcceptable(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest
      request) {
    return super.handleMissingPathVariable(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus
      status, WebRequest request) {
    return super.handleMissingServletRequestParameter(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status,
                                                                        WebRequest request) {
    return super.handleServletRequestBindingException(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status,
                                                                WebRequest request) {
    return super.handleConversionNotSupported(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status,
                                                                WebRequest request) {
    return super.handleHttpMessageNotWritable(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status,
                                                                   WebRequest request) {
    return super.handleMissingServletRequestPart(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    //Get all fields errors
    List<String> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.toList());

    return buildResponseEntity(
        ApiError.builder().message("Invalid request!").build(),
        HttpStatus.BAD_REQUEST
    );

  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest
      request) {
    return super.handleNoHandlerFoundException(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status,
                                                                      WebRequest webRequest) {
    return super.handleAsyncRequestTimeoutException(ex, headers, status, webRequest);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
    return super.handleExceptionInternal(ex, body, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    return super.handleTypeMismatch(ex, headers, status, request);
  }


  private ResponseEntity<Object> buildResponseEntity(ApiError apiError, HttpStatus httpStatus) {
    return ResponseEntity.status(httpStatus).body(apiError);
  }


  @ExceptionHandler(EntityNotFoundException.class)
  protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
    return buildResponseEntity(
        ApiError.builder().message(ex.getMessage()).build(),
        HttpStatus.NOT_FOUND
    );
  }

  @ExceptionHandler(IllegalArgumentException.class)
  protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
    return buildResponseEntity(
        ApiError.builder().message(ex.getMessage()).build(),
        HttpStatus.BAD_REQUEST
    );
  }

  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {

    return buildResponseEntity(
        ApiError.builder().message("Invalid request!").build(),
        HttpStatus.BAD_REQUEST
    );
  }

  @ExceptionHandler(EntityExistsException.class)
  protected ResponseEntity<Object> handleDataIntegrityViolationException(EntityExistsException ex) {

    return buildResponseEntity(
        ApiError.builder().message(ex.getMessage()).build(),
        HttpStatus.BAD_REQUEST
    );
  }


  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers,

                                                                HttpStatus status, WebRequest request) {
    //Get all validation errors
    List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());

    //Take first
    return buildResponseEntity(
            ApiError.builder().message(errors.get(0)).build(),
            HttpStatus.BAD_REQUEST
    );

  }

}
