package org.garvk.authservice.advice;

import org.apache.coyote.Response;
import org.garvk.authservice.exception.AuthServiceException;
import org.garvk.authservice.exception.EmailAlreadyExistsException;
import org.garvk.authservice.exception.UserNotFoundException;
import org.garvk.authservice.model.ErrorResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Resource Already Exists either by Username or Email
     * @param ex
     * @param req
     * @return
     */
    @ExceptionHandler({EmailAlreadyExistsException.class, UserNotFoundException.class})
    public ResponseEntity<ErrorResponseDto> handleResourceAlreadyExistException(AuthServiceException ex, WebRequest req){
        return createErrorResponse(ex, HttpStatus.CONFLICT, req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAllExceptions(Exception ex, WebRequest req){
        logger.error("Unexpected Error Occured", ex);

        return createErrorResponse(
                new Exception("Unexpected Error Occured"),
                HttpStatus.INTERNAL_SERVER_ERROR,
                req
        );

    }


    /**
     * Utility Method to make ErrorResponses for all Exceptions
     * @param aInException
     * @param aInStatus
     * @param aInRequest
     * @return
     */
    private ResponseEntity<ErrorResponseDto> createErrorResponse(Exception aInException, HttpStatus aInStatus, WebRequest aInRequest){
        String path = ((ServletWebRequest) aInRequest).getRequest().getRequestURI();

        ErrorResponseDto aInOutErrorResponse = new ErrorResponseDto(
                LocalDateTime.now().toString(),
                aInStatus.value(),
                aInStatus.getReasonPhrase(),
                aInException.getMessage(),
                path
        );

        return new ResponseEntity<>(aInOutErrorResponse, aInStatus);
    }
}
