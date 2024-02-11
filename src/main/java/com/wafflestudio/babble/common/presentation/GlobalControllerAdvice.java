package com.wafflestudio.babble.common.presentation;

import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.wafflestudio.babble.common.exception.BadRequestException;
import com.wafflestudio.babble.common.exception.ForbiddenException;
import com.wafflestudio.babble.common.exception.NotFoundException;
import com.wafflestudio.babble.common.exception.UnauthenticatedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "알 수 없는 문제가 발생했습니다.";
    private static final String INVALID_REQUEST_EXCEPTION_MESSAGE_FORMAT = "잘못된 요청입니다. %s";
    private static final String INVALID_REQUEST_DELIMITER = " ";

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("UnhandledException has been thrown", e);
        return new ResponseEntity<>(new ErrorResponse(INTERNAL_SERVER_ERROR_MESSAGE), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(BadRequestException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException bindingResult) {
        String causes = bindingResult.getFieldErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining(INVALID_REQUEST_DELIMITER));
        String errorMessage = String.format(INVALID_REQUEST_EXCEPTION_MESSAGE_FORMAT, causes);
        return new ResponseEntity<>(new ErrorResponse(errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(UnauthenticatedException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(ForbiddenException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(NotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
