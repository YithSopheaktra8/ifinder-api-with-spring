package co.istad.ifinder.exception;

import co.istad.ifinder.base.BaseError;
import co.istad.ifinder.base.BaseErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JwtException {

    @ExceptionHandler(JwtValidationException.class)
    public ResponseEntity<?> handleServiceError(JwtValidationException ex) {

        BaseError<String> baseError = new BaseError<>();
        baseError.setCode(HttpStatus.UNAUTHORIZED.toString());
        baseError.setDescription(ex.getMessage());

        BaseErrorResponse baseErrorResponse = BaseErrorResponse.builder()
                .Error(baseError)
                .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(baseErrorResponse);
    }

}
