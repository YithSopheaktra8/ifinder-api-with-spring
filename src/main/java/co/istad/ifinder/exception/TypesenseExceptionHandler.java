package co.istad.ifinder.exception;


import co.istad.ifinder.base.BaseError;
import co.istad.ifinder.base.BaseErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.typesense.api.exceptions.TypesenseError;

@RestControllerAdvice
public class TypesenseExceptionHandler {

    @ExceptionHandler(TypesenseError.class)
    public ResponseEntity<?> handleServiceError(TypesenseError ex) {

        BaseError<String> baseError = new BaseError<>();
        baseError.setCode(String.valueOf(ex.status));
        baseError.setDescription(ex.message);

        BaseErrorResponse baseErrorResponse = BaseErrorResponse.builder()
                .Error(baseError)
                .build();

        return ResponseEntity
                .status(ex.status)
                .body(baseErrorResponse);
    }
}
