package com.esm.epam.handler;

import com.esm.epam.entity.ErrorResponse;
import com.esm.epam.exception.ControllerException;
import com.esm.epam.exception.DaoException;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.exception.ServiceException;
import org.hibernate.TransientObjectException;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public final ErrorResponse handleResourceNotFoundException(ResourceNotFoundException exception) {
        return new ErrorResponse(1, exception.getLocalizedMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(BAD_REQUEST)
    public final ErrorResponse handleDuplicateKeyException(DuplicateKeyException exception) {
        return new ErrorResponse(2, exception.getLocalizedMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public final ErrorResponse handleConstraintViolationException(ConstraintViolationException exception) {
        return new ErrorResponse(3, exception.getMessage());
    }

    @ExceptionHandler(ControllerException.class)
    @ResponseStatus(NOT_ACCEPTABLE)
    public final ErrorResponse handleControllerException(ControllerException exception) {
        return new ErrorResponse(4, exception.getLocalizedMessage());
    }

    @ExceptionHandler(DaoException.class)
    @ResponseStatus(BAD_REQUEST)
    public final ErrorResponse handleDaoException(DaoException exception) {
        return new ErrorResponse(5, exception.getLocalizedMessage());
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(BAD_REQUEST)
    public final ErrorResponse handleServiceException(ServiceException exception) {
        return new ErrorResponse(6, exception.getLocalizedMessage());

    }

    @ExceptionHandler(PSQLException.class)
    @ResponseStatus(BAD_REQUEST)
    public final ErrorResponse handlePSQLException(PSQLException exception) {
        return new ErrorResponse(7, exception.getLocalizedMessage());
    }

    @ExceptionHandler(TransientObjectException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public final ErrorResponse handleTransientObjectException(TransientObjectException exception) {
        return new ErrorResponse(8, exception.getLocalizedMessage());

    }
}