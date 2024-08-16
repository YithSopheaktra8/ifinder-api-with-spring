package co.istad.ifinder.exception;

import co.istad.ifinder.base.BaseError;
import co.istad.ifinder.base.BaseErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ValidationException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseErrorResponse handleValidationError(MethodArgumentNotValidException ex) {

        BaseError<List<?>> baseError = new BaseError<>();

        List<Map<String, Object>> errorList = new ArrayList<>();

        ex.getFieldErrors()
                .forEach(fieldError -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("field", fieldError.getField());
                    error.put("message", fieldError.getDefaultMessage());
                    errorList.add(error);
                });


        baseError.setCode(HttpStatus.BAD_REQUEST.getReasonPhrase());
        baseError.setDescription(errorList);

        return new BaseErrorResponse(baseError);
    }
}