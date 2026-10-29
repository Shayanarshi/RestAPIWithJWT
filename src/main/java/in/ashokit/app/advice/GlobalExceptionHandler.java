package in.ashokit.app.advice;

import in.ashokit.app.exception.ResourceNotFoundException;
import in.ashokit.app.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        List<FieldError> list = ex.getBindingResult().getFieldErrors();
        list.forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return errors;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                         HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorResponse.setErrorMessage(ex.getMessage());
        errorResponse.setTimestamp(Instant.now());
        errorResponse.setRequestUri(request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}