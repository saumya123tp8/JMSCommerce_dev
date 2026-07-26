package com.example.JMSCommerce.Exception;

import com.example.JMSCommerce.Utility.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CompulsoryDataMissingException.class)
    public ResponseEntity<ApiResponse<Void>> handleCompulsoryDataMissing(CompulsoryDataMissingException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage(),"Compulsory Data missing",null));
    }


    @ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateRecord(DuplicateRecordException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage(),"Duplicate record exists",null));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> ResourceNotFoundExceptionHandler(ResourceNotFoundException ex){
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage(),"Resource Not Found",""));
    }

    @ExceptionHandler(ResourceDeletionException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceDeletionException(ResourceDeletionException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage(), "Delete operation failed",""));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), "Bad request",""));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "Invalid value '" + ex.getValue() + "' for parameter '" + ex.getName()
                + "'. Expected type: " + ex.getRequiredType().getSimpleName();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(message, "Invalid request parameter",""));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAllGeneralExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Something went wrong", "Internal server error","" +
                        ""));
    }

    @ExceptionHandler(
            {
                    BadCredentialsException.class,
                    BadCredentialsCustomException.class
            }
    )
    public ResponseEntity<ApiResponse<Void>> handleBadCredentialException(BadCredentialsCustomException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage(),"Bad Request",null));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponse<?>> handleValidationException(
            MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        StringBuffer concatErrors = new StringBuffer("");
        for(String err : errors){
            concatErrors.append(err);
        }
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(
                        "Validation Failed",
                        concatErrors.toString(),
                        null
                ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(
                        ApiResponse.error(
                                "Access Denied",
                                "You don't have permission to perform this action.",
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex) {

        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException invalidFormatException) {

            if (invalidFormatException.getTargetType().isEnum()) {

                String field =
                        invalidFormatException.getPath()
                                .get(0).getPropertyName();

                String invalidValue =
                        String.valueOf(invalidFormatException.getValue());

                String allowedValues =
                        Arrays.stream(invalidFormatException.getTargetType().getEnumConstants())
                                .map(Object::toString)
                                .collect(Collectors.joining(", "));

                return ResponseEntity.badRequest().body(
                        ApiResponse.error(
                                "Invalid Request",
                                field + " has invalid value '" +
                                        invalidValue +
                                        "'. Allowed values: [" +
                                        allowedValues +
                                        "]",

                                null
                        )
                );
            }
        }

        return ResponseEntity.badRequest().body(
                ApiResponse.error("Inavlid Request","Malformed JSON request.", null)
        );
    }

}
